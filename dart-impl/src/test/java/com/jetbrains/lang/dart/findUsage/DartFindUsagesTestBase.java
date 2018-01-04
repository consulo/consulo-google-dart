package com.jetbrains.lang.dart.findUsage;

import java.util.Collection;

import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase;
import com.intellij.usageView.UsageInfo;
import com.intellij.usages.PsiElementUsageTarget;
import com.intellij.usages.UsageTarget;
import com.intellij.usages.UsageTargetUtil;
import com.jetbrains.lang.dart.util.DartTestUtils;

abstract public class DartFindUsagesTestBase extends CodeInsightFixtureTestCase {

  @Override
  protected String getBasePath() {
    return FileUtil.toSystemDependentName(DartTestUtils.RELATIVE_TEST_DATA_PATH + "/findUsages/");
  }

  protected void doTest(int size) throws Throwable {
    doTest(size, getTestName(false) + ".dart");
  }

  protected void doTest(int size, String... files) throws Throwable {
    myFixture.configureByFiles(files);
    doTestInner(size);
  }

  protected void doTestInner(int size) throws Throwable {
    final Collection<UsageInfo> elements = findUsages();
    assertNotNull(elements);
    assertEquals(size, elements.size());
  }

  private Collection<UsageInfo> findUsages()
    throws Throwable {
    final UsageTarget[] targets = UsageTargetUtil.findUsageTargets(dataId -> ((EditorEx)myFixture.getEditor()).getDataContext().getData(dataId));

    assert targets != null && targets.length > 0 && targets[0] instanceof PsiElementUsageTarget;
    return myFixture.findUsages(((PsiElementUsageTarget)targets[0]).getElement());
  }
}
