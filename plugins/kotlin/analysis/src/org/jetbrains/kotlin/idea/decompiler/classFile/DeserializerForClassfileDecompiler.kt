// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package org.jetbrains.kotlin.idea.decompiler.classFile

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.kotlin.builtins.DefaultBuiltIns
import org.jetbrains.kotlin.builtins.KotlinBuiltIns
import org.jetbrains.kotlin.contracts.ContractDeserializerImpl
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.NotFoundClasses
import org.jetbrains.kotlin.idea.caches.IDEKotlinBinaryClassCache
import org.jetbrains.kotlin.idea.decompiler.textBuilder.DeserializerForDecompilerBase
import org.jetbrains.kotlin.idea.decompiler.textBuilder.LoggingErrorReporter
import org.jetbrains.kotlin.idea.decompiler.textBuilder.ResolveEverythingToKotlinAnyLocalClassifierResolver
import org.jetbrains.kotlin.incremental.components.LookupTracker
import org.jetbrains.kotlin.load.java.structure.JavaClass
import org.jetbrains.kotlin.load.java.structure.classId
import org.jetbrains.kotlin.load.kotlin.*
import org.jetbrains.kotlin.load.kotlin.KotlinClassFinder.Result.KotlinClass
import org.jetbrains.kotlin.metadata.jvm.deserialization.JvmProtoBufUtil
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.resolve.sam.SamConversionResolverImpl
import org.jetbrains.kotlin.serialization.deserialization.ClassData
import org.jetbrains.kotlin.serialization.deserialization.ClassDataFinder
import org.jetbrains.kotlin.serialization.deserialization.DeserializationComponents
import org.jetbrains.kotlin.serialization.deserialization.DeserializationConfiguration
import org.jetbrains.kotlin.serialization.deserialization.descriptors.DeserializedPackageMemberScope
import java.io.InputStream

fun DeserializerForClassfileDecompiler(classFile: VirtualFile): DeserializerForClassfileDecompiler {
    val kotlinClassHeaderInfo =
        IDEKotlinBinaryClassCache.getInstance().getKotlinBinaryClassHeaderData(classFile)
            ?: error("Decompiled data factory shouldn't be called on an unsupported file: $classFile")
    val packageFqName = kotlinClassHeaderInfo.classId.packageFqName
    return DeserializerForClassfileDecompiler(classFile.parent!!, packageFqName)
}

class DeserializerForClassfileDecompiler(
    packageDirectory: VirtualFile,
    directoryPackageFqName: FqName
) : DeserializerForDecompilerBase(directoryPackageFqName) {
    override val builtIns: KotlinBuiltIns get() = DefaultBuiltIns.Instance

    private val classFinder = DirectoryBasedClassFinder(packageDirectory, directoryPackageFqName)

    override val deserializationComponents: DeserializationComponents

    init {
        val classDataFinder = DirectoryBasedDataFinder(classFinder, LOG)
        val notFoundClasses = NotFoundClasses(storageManager, moduleDescriptor)
        val annotationAndConstantLoader =
            BinaryClassAnnotationAndConstantLoaderImpl(moduleDescriptor, notFoundClasses, storageManager, classFinder)

        val configuration = object : DeserializationConfiguration {
            override val readDeserializedContracts: Boolean = true
            override val preserveDeclarationsOrdering: Boolean = true
        }

        deserializationComponents = DeserializationComponents(
            storageManager, moduleDescriptor, configuration, classDataFinder, annotationAndConstantLoader,
            packageFragmentProvider, ResolveEverythingToKotlinAnyLocalClassifierResolver(builtIns), LoggingErrorReporter(LOG),
            LookupTracker.DO_NOTHING, JavaFlexibleTypeDeserializer, emptyList(), notFoundClasses,
            ContractDeserializerImpl(configuration, storageManager),
            extensionRegistryLite = JvmProtoBufUtil.EXTENSION_REGISTRY,
            samConversionResolver = SamConversionResolverImpl(storageManager, samWithReceiverResolvers = emptyList())
        )
    }

    override fun resolveDeclarationsInFacade(facadeFqName: FqName): List<DeclarationDescriptor> {
        val packageFqName = facadeFqName.parent()
        assert(packageFqName == directoryPackageFqName) {
            "Was called for $facadeFqName; only members of $directoryPackageFqName package are expected."
        }
        val binaryClassForPackageClass = classFinder.findKotlinClass(ClassId.topLevel(facadeFqName))
        val header = binaryClassForPackageClass?.classHeader
        val annotationData = header?.data
        val strings = header?.strings
        if (annotationData == null || strings == null) {
            LOG.error("Could not read annotation data for $facadeFqName from ${binaryClassForPackageClass?.classId}")
            return emptyList()
        }
        val (nameResolver, packageProto) = JvmProtoBufUtil.readPackageDataFrom(annotationData, strings)
        val dummyPackageFragment = createDummyPackageFragment(header.packageName?.let(::FqName) ?: facadeFqName.parent())
        val membersScope = DeserializedPackageMemberScope(
            dummyPackageFragment,
            packageProto, nameResolver, header.metadataVersion,
            JvmPackagePartSource(binaryClassForPackageClass, packageProto, nameResolver), deserializationComponents,
            "scope of dummyPackageFragment ${dummyPackageFragment.fqName} in module $moduleDescriptor @DeserializerForClassfileDecompiler"
        ) { emptyList() }
        return membersScope.getContributedDescriptors().toList()
    }

    companion object {
        private val LOG = Logger.getInstance(DeserializerForClassfileDecompiler::class.java)
    }
}

class DirectoryBasedClassFinder(
    val packageDirectory: VirtualFile,
    val directoryPackageFqName: FqName
) : KotlinClassFinder {
    override fun findKotlinClassOrContent(javaClass: JavaClass) = findKotlinClassOrContent(javaClass.classId!!)

    override fun findKotlinClassOrContent(classId: ClassId): KotlinClassFinder.Result? {
        if (classId.packageFqName != directoryPackageFqName) {
            return null
        }
        val targetName = classId.relativeClassName.pathSegments().joinToString("$", postfix = ".class")
        val virtualFile = packageDirectory.findChild(targetName)
        if (virtualFile != null && isKotlinWithCompatibleAbiVersion(virtualFile)) {
            return IDEKotlinBinaryClassCache.getInstance().getKotlinBinaryClass(virtualFile)?.let(::KotlinClass)
        }
        return null
    }

    // TODO
    override fun findMetadata(classId: ClassId): InputStream? = null

    // TODO
    override fun hasMetadataPackage(fqName: FqName): Boolean = false

    // TODO: load built-ins from packageDirectory?
    override fun findBuiltInsData(packageFqName: FqName): InputStream? = null
}

class DirectoryBasedDataFinder(
    val classFinder: DirectoryBasedClassFinder,
    val log: Logger
) : ClassDataFinder {
    override fun findClassData(classId: ClassId): ClassData? {
        val binaryClass = classFinder.findKotlinClass(classId) ?: return null
        val classHeader = binaryClass.classHeader
        val data = classHeader.data
        if (data == null) {
            log.error("Annotation data missing for ${binaryClass.classId}")
            return null
        }
        val strings = classHeader.strings
        if (strings == null) {
            log.error("String table not found in class ${binaryClass.classId}")
            return null
        }

        val (nameResolver, classProto) = JvmProtoBufUtil.readClassDataFrom(data, strings)
        return ClassData(nameResolver, classProto, classHeader.metadataVersion, KotlinJvmBinarySourceElement(binaryClass))
    }
}
