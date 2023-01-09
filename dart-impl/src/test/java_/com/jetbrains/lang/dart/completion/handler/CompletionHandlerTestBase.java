package com.jetbrains.lang.dart.completion.handler;

import consulo.language.editor.completion.CompletionType;
import consulo.language.editor.completion.lookup.LookupManager;
import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase;

public abstract class CompletionHandlerTestBase extends CodeInsightFixtureTestCase {
  protected void doTest() {
    doTest(CompletionType.BASIC);
  }

  protected void doTest(CompletionType type) {
    configure();
    myFixture.complete(type);
    if (LookupManager.getActiveLookup(myFixture.getEditor()) != null) {
      myFixture.type('\n');
    }
    myFixture.checkResultByFile(getTestName(true) + ".after" + getTestFileExtension());
  }

  protected void configure() {
    myFixture.configureByFile(getTestName(true) + getTestFileExtension());
  }

  protected abstract String getTestFileExtension();
}
