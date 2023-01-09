package com.jetbrains.lang.dart.refactoring.extract;

import consulo.ide.impl.idea.openapi.util.io.FileUtil;
import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase;
import com.jetbrains.lang.dart.ide.refactoring.extract.DartExtractMethodHandler;
import com.jetbrains.lang.dart.util.DartSdkTestUtil;
import com.jetbrains.lang.dart.util.DartTestUtils;

/**
 * @author: Fedor.Korotkov
 */
public abstract class DartExtractMethodInHtmlTest extends CodeInsightFixtureTestCase {
  @Override
  protected String getBasePath() {
    return consulo.ide.impl.idea.openapi.util.io.FileUtil.toSystemDependentName(DartTestUtils.RELATIVE_TEST_DATA_PATH + "/refactoring/extractMethod/html/");
  }

  private void doTest() throws Throwable {
    DartSdkTestUtil.configFakeSdk(myFixture);
    myFixture.configureByFile(getTestName(true) + ".html");
    doTestImpl();
  }

  private void doTestImpl() {
    final DartExtractMethodHandler extractMethodHandler = new DartExtractMethodHandler();
    //noinspection NullableProblems
    extractMethodHandler.invoke(myFixture.getProject(), myFixture.getEditor(), myFixture.getFile(), null);
    myFixture.checkResultByFile(getTestName(true) + "_expected.html");
  }

  public void testExtract1() throws Throwable {
    doTest();
  }
}
