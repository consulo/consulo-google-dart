package com.jetbrains.lang.dart.refactoring.introduce;

import consulo.ide.impl.idea.openapi.util.io.FileUtil;
import com.jetbrains.lang.dart.ide.refactoring.introduce.DartIntroduceHandler;
import com.jetbrains.lang.dart.ide.refactoring.introduce.DartIntroduceVariableHandler;
import com.jetbrains.lang.dart.psi.DartCallExpression;
import com.jetbrains.lang.dart.util.DartTestUtils;

/**
 * @author: Fedor.Korotkov
 */
public abstract class DartIntroduceVariableInHtmlTest extends DartIntroduceTestBase {
  @Override
  protected String getBasePath() {
    return consulo.ide.impl.idea.openapi.util.io.FileUtil.toSystemDependentName(DartTestUtils.RELATIVE_TEST_DATA_PATH + "/refactoring/introduceVariable/html/");
  }

  @Override
  protected DartIntroduceHandler createHandler() {
    return new DartIntroduceVariableHandler();
  }

  @Override
  protected String getFileExtension() {
    return ".html";
  }

  public void testReplaceAll1() throws Throwable {
    doTest();
  }

  public void testReplaceOne1() throws Throwable {
    doTest(null, false);
  }

  public void testSuggestName1() throws Throwable {
    doTestSuggestions(DartCallExpression.class, "test");
  }
}
