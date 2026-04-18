package com.jetbrains.lang.dart.completion.handler;

import com.jetbrains.lang.dart.util.DartTestUtils;
import consulo.util.io.FileUtil;

/**
 * @author Fedor.Korotkov
 */
public abstract class CompletionHandlerInHtmlTest extends CompletionHandlerTestBase {
  @Override
  protected String getBasePath() {
    return FileUtil.toSystemDependentName(DartTestUtils.RELATIVE_TEST_DATA_PATH + "/completion/handler/html/");
  }

  @Override
  protected String getTestFileExtension() {
    return ".html";
  }

  public void testParentheses1() throws Throwable {
    doTest();
  }
}
