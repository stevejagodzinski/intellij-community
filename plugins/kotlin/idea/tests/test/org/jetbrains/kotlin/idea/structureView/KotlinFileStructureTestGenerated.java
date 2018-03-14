/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.structureView;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.KotlinTestUtils;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("idea/testData/structureView/fileStructure")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class KotlinFileStructureTestGenerated extends AbstractKotlinFileStructureTest {
    public void testAllFilesPresentInFileStructure() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/structureView/fileStructure"), Pattern.compile("^([^.]+)\\.kt$"), TargetBackend.ANY, true);
    }

    @TestMetadata("AnonymousObjectMembers.kt")
    public void testAnonymousObjectMembers() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/structureView/fileStructure/AnonymousObjectMembers.kt");
        doTest(fileName);
    }

    @TestMetadata("CheckLocationForKotlin.kt")
    public void testCheckLocationForKotlin() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/structureView/fileStructure/CheckLocationForKotlin.kt");
        doTest(fileName);
    }

    @TestMetadata("CheckMemberLocationForJava.kt")
    public void testCheckMemberLocationForJava() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/structureView/fileStructure/CheckMemberLocationForJava.kt");
        doTest(fileName);
    }

    @TestMetadata("DoNotShowParentsInLocationJava.kt")
    public void testDoNotShowParentsInLocationJava() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/structureView/fileStructure/DoNotShowParentsInLocationJava.kt");
        doTest(fileName);
    }

    @TestMetadata("EmptyFile.kt")
    public void testEmptyFile() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/structureView/fileStructure/EmptyFile.kt");
        doTest(fileName);
    }

    @TestMetadata("InheritedDelegationMethods.kt")
    public void testInheritedDelegationMethods() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/structureView/fileStructure/InheritedDelegationMethods.kt");
        doTest(fileName);
    }

    @TestMetadata("InheritedInnerClasses.kt")
    public void testInheritedInnerClasses() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/structureView/fileStructure/InheritedInnerClasses.kt");
        doTest(fileName);
    }

    @TestMetadata("InheritedJavaMembers.kt")
    public void testInheritedJavaMembers() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/structureView/fileStructure/InheritedJavaMembers.kt");
        doTest(fileName);
    }

    @TestMetadata("InheritedLocalKotlin.kt")
    public void testInheritedLocalKotlin() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/structureView/fileStructure/InheritedLocalKotlin.kt");
        doTest(fileName);
    }

    @TestMetadata("InheritedMembers.kt")
    public void testInheritedMembers() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/structureView/fileStructure/InheritedMembers.kt");
        doTest(fileName);
    }

    @TestMetadata("InheritedMembersOfEnum.kt")
    public void testInheritedMembersOfEnum() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/structureView/fileStructure/InheritedMembersOfEnum.kt");
        doTest(fileName);
    }

    @TestMetadata("InheritedMembersWithSubstitutedTypes.kt")
    public void testInheritedMembersWithSubstitutedTypes() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/structureView/fileStructure/InheritedMembersWithSubstitutedTypes.kt");
        doTest(fileName);
    }

    @TestMetadata("InheritedSAMConversion.kt")
    public void testInheritedSAMConversion() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/structureView/fileStructure/InheritedSAMConversion.kt");
        doTest(fileName);
    }

    @TestMetadata("InheritedSynthesizedFromDataClass.kt")
    public void testInheritedSynthesizedFromDataClass() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/structureView/fileStructure/InheritedSynthesizedFromDataClass.kt");
        doTest(fileName);
    }

    @TestMetadata("LocalElements.kt")
    public void testLocalElements() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/structureView/fileStructure/LocalElements.kt");
        doTest(fileName);
    }

    @TestMetadata("Render.kt")
    public void testRender() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/structureView/fileStructure/Render.kt");
        doTest(fileName);
    }

    @TestMetadata("SeveralClasses.kt")
    public void testSeveralClasses() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/structureView/fileStructure/SeveralClasses.kt");
        doTest(fileName);
    }

    @TestMetadata("Simple.kt")
    public void testSimple() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/structureView/fileStructure/Simple.kt");
        doTest(fileName);
    }
}
