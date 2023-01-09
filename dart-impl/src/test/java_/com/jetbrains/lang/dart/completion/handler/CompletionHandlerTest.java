package com.jetbrains.lang.dart.completion.handler;

import consulo.ide.impl.idea.openapi.util.io.FileUtil;
import com.jetbrains.lang.dart.util.DartTestUtils;

/**
 * @author: Fedor.Korotkov
 */
public abstract class CompletionHandlerTest extends CompletionHandlerTestBase {
  @Override
  protected String getBasePath() {
    return consulo.ide.impl.idea.openapi.util.io.FileUtil.toSystemDependentName(DartTestUtils.RELATIVE_TEST_DATA_PATH + "/completion/handler");
  }

  @Override
  protected String getTestFileExtension() {
    return ".dart";
  }

  public void testConstructor1() throws Throwable {
    doTest();
  }

  public void testConstructor2() throws Throwable {
    doTest();
  }

  public void testConstructor3() throws Throwable {
    doTest();
  }

  public void testParentheses1() throws Throwable {
    doTest();
  }

  public void testParentheses2() throws Throwable {
    doTest();
  }
}
