package com.jetbrains.lang.dart.resolve;

import java.io.IOException;
import java.util.Collection;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase;
import com.jetbrains.lang.dart.util.DartTestUtils;
import consulo.codeInsight.TargetElementUtil;

abstract public class DartGotoDeclarationActionTestBase extends CodeInsightFixtureTestCase {
  @Override
  protected String getBasePath() {
    return FileUtil.toSystemDependentName(DartTestUtils.RELATIVE_TEST_DATA_PATH + "/goto/");
  }

  protected void doTest(int expectedSize) throws IOException {
    doTest(expectedSize, getTestName(true) + ".dart");
  }

  protected void doTest(int expectedSize, String... files) throws IOException {
    doTest(myFixture.configureByFiles(files), expectedSize);
  }

  protected void doTest(PsiFile[] files, int expectedSize) {
    doTest(files[0], expectedSize);
  }

  protected void doTest(PsiFile myFile, int expectedSize) {
    final Collection<PsiElement> elements = TargetElementUtil.getTargetCandidates(myFile.findReferenceAt(myFixture.getCaretOffset()));
    assertNotNull(elements);
    assertEquals(expectedSize, elements.size());
  }
}
