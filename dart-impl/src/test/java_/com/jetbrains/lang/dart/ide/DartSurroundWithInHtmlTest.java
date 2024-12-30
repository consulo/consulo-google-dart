package com.jetbrains.lang.dart.ide;

import consulo.ide.impl.idea.codeInsight.generation.surroundWith.SurroundWithHandler;
import consulo.language.editor.surroundWith.Surrounder;
import consulo.ide.impl.idea.openapi.util.io.FileUtil;
import com.intellij.testFramework.LightPlatformCodeInsightTestCase;
import com.jetbrains.lang.dart.ide.surroundWith.statement.DartWithIfElseSurrounder;
import com.jetbrains.lang.dart.util.DartTestUtils;

import jakarta.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
public abstract class DartSurroundWithInHtmlTest extends LightPlatformCodeInsightTestCase {
  @Nonnull
  @Override
  protected String getTestDataPath() {
    return DartTestUtils.BASE_TEST_DATA_PATH + consulo.ide.impl.idea.openapi.util.io.FileUtil.toSystemDependentName("/surroundWith/html/");
  }

  private void doTest(final Surrounder handler) throws Exception {
    configureByFile(getTestName(false) + ".html");
    consulo.ide.impl.idea.codeInsight.generation.surroundWith.SurroundWithHandler.invoke(getProject(), getEditor(), getFile(), handler);
    checkResultByFile(getTestName(false) + ".after.html");
  }

  public void testIfElse1() throws Throwable {
    doTest(new DartWithIfElseSurrounder());
  }
}
