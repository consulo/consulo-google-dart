package com.jetbrains.lang.dart.completion.editor;

import consulo.language.editor.completion.CompletionType;
import consulo.ide.impl.idea.openapi.util.io.FileUtil;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import com.jetbrains.lang.dart.util.DartTestUtils;
import consulo.language.editor.completion.lookup.Lookup;

/**
 * @author: Fedor.Korotkov
 */
public abstract class DartTabCompletionTest extends LightPlatformCodeInsightFixtureTestCase {
  @Override
  protected String getTestDataPath() {
    return FileUtil.toSystemDependentName(DartTestUtils.BASE_TEST_DATA_PATH + "/completion/tab");
  }

  public void doTest() {
    myFixture.configureByFile(getTestName(true) + ".dart");
    myFixture.complete(CompletionType.BASIC);
    myFixture.finishLookup(Lookup.REPLACE_SELECT_CHAR);
    myFixture.checkResultByFile(getTestName(true) + "_expected.dart");
  }

  public void testExpression1() {
    doTest();
  }

  public void testWEB_7191() {
    doTest();
  }
}
