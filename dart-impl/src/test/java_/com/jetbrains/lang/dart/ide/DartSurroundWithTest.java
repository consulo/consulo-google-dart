package com.jetbrains.lang.dart.ide;

import consulo.ide.impl.idea.codeInsight.generation.surroundWith.SurroundWithHandler;
import consulo.ide.impl.idea.openapi.util.io.FileUtil;
import com.intellij.testFramework.LightPlatformCodeInsightTestCase;
import com.jetbrains.lang.dart.ide.surroundWith.expression.DartWithNotParenthesisExpressionSurrounder;
import com.jetbrains.lang.dart.ide.surroundWith.expression.DartWithParenthesisExpressionSurrounder;
import com.jetbrains.lang.dart.ide.surroundWith.statement.*;
import com.jetbrains.lang.dart.util.DartTestUtils;
import consulo.language.editor.surroundWith.Surrounder;

import jakarta.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
public abstract class DartSurroundWithTest extends LightPlatformCodeInsightTestCase {
  @Nonnull
  @Override
  protected String getTestDataPath() {
    return DartTestUtils.BASE_TEST_DATA_PATH + consulo.ide.impl.idea.openapi.util.io.FileUtil.toSystemDependentName("/surroundWith/");
  }

  private void doTest(final Surrounder handler) throws Exception {
    configureByFile(getTestName(false) + ".dart");
    consulo.ide.impl.idea.codeInsight.generation.surroundWith.SurroundWithHandler.invoke(getProject(), getEditor(), getFile(), handler);
    checkResultByFile(getTestName(false) + ".after.dart");
  }

  public void testDoWhile1() throws Throwable {
    doTest(new DartWithDoWhileSurrounder());
  }

  public void testDoWhile2() throws Throwable {
    doTest(new DartWithDoWhileSurrounder());
  }


  public void testFor1() throws Throwable {
    doTest(new DartWithForSurrounder());
  }

  public void testFor2() throws Throwable {
    doTest(new DartWithForSurrounder());
  }

  public void testNotParenthesis1() throws Throwable {
    doTest(new DartWithNotParenthesisExpressionSurrounder());
  }

  public void testNotParenthesis2() throws Throwable {
    doTest(new DartWithNotParenthesisExpressionSurrounder());
  }

  public void testIf1() throws Throwable {
    doTest(new DartWithIfSurrounder());
  }

  public void testIf2() throws Throwable {
    doTest(new DartWithIfSurrounder());
  }

  public void testIfElse1() throws Throwable {
    doTest(new DartWithIfElseSurrounder());
  }

  public void testIfElse2() throws Throwable {
    doTest(new DartWithIfElseSurrounder());
  }

  public void testParenthesis1() throws Throwable {
    doTest(new DartWithParenthesisExpressionSurrounder());
  }

  public void testParenthesis2() throws Throwable {
    doTest(new DartWithParenthesisExpressionSurrounder());
  }

  public void testTryCatch() throws Throwable {
    doTest(new DartWithTryCatchSurrounder());
  }

  public void testTryCatchFinally() throws Throwable {
    doTest(new DartWithTryCatchFinallySurrounder());
  }

  public void testWhile1() throws Throwable {
    doTest(new DartWithWhileSurrounder());
  }

  public void testWhile2() throws Throwable {
    doTest(new DartWithWhileSurrounder());
  }
}
