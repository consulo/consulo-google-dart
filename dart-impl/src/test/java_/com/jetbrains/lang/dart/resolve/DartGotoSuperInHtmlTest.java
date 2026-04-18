package com.jetbrains.lang.dart.resolve;

import consulo.language.editor.action.CodeInsightActionHandler;
import com.intellij.lang.CodeInsightActions;
import consulo.ide.impl.idea.openapi.application.PathManager;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.util.DartTestUtils;
import consulo.util.io.FileUtil;

/**
 * @author Fedor.Korotkov
 */
public abstract class DartGotoSuperInHtmlTest extends LightPlatformCodeInsightFixtureTestCase {
  @Override
  protected String getTestDataPath() {
    return DartTestUtils.BASE_TEST_DATA_PATH + FileUtil.toSystemDependentName("/gotoSuper/html");
  }

  private void doTest() throws Throwable {
    myFixture.configureByFile(getTestName(false) + ".html");
    final CodeInsightActionHandler handler = CodeInsightActions.GOTO_SUPER.forLanguage(DartLanguage.INSTANCE);
    handler.invoke(getProject(), myFixture.getEditor(), myFixture.getFile());
    myFixture.checkResultByFile(getTestName(false) + ".txt");
  }

  public void testGts1() throws Throwable {
    doTest();
  }

  public void testGts2() throws Throwable {
    doTest();
  }
}
