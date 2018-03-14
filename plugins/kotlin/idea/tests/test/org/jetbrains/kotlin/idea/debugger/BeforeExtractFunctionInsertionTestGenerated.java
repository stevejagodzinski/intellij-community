/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.debugger;

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
@TestMetadata("idea/testData/debugger/insertBeforeExtractFunction")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class BeforeExtractFunctionInsertionTestGenerated extends AbstractBeforeExtractFunctionInsertionTest {
    public void testAllFilesPresentInInsertBeforeExtractFunction() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/debugger/insertBeforeExtractFunction"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
    }

    @TestMetadata("emptyImportDirective.kt")
    public void testEmptyImportDirective() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/insertBeforeExtractFunction/emptyImportDirective.kt");
        doTest(fileName);
    }

    @TestMetadata("emptyImportDirective2.kt")
    public void testEmptyImportDirective2() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/insertBeforeExtractFunction/emptyImportDirective2.kt");
        doTest(fileName);
    }

    @TestMetadata("emptyPackageDirective.kt")
    public void testEmptyPackageDirective() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/insertBeforeExtractFunction/emptyPackageDirective.kt");
        doTest(fileName);
    }

    @TestMetadata("emptyPackageDirective2.kt")
    public void testEmptyPackageDirective2() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/insertBeforeExtractFunction/emptyPackageDirective2.kt");
        doTest(fileName);
    }

    @TestMetadata("manyImports.kt")
    public void testManyImports() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/debugger/insertBeforeExtractFunction/manyImports.kt");
        doTest(fileName);
    }
}
