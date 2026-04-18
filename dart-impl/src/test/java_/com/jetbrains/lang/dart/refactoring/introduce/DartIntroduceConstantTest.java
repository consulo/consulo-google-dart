package com.jetbrains.lang.dart.refactoring.introduce;

import com.jetbrains.lang.dart.ide.refactoring.introduce.DartIntroduceFinalVariableHandler;
import com.jetbrains.lang.dart.ide.refactoring.introduce.DartIntroduceHandler;
import com.jetbrains.lang.dart.util.DartTestUtils;
import consulo.util.io.FileUtil;

/**
 * @author Fedor.Korotkov
 */
public abstract class DartIntroduceConstantTest extends DartIntroduceTestBase {
  @Override
  protected String getBasePath() {
    return FileUtil.toSystemDependentName(DartTestUtils.RELATIVE_TEST_DATA_PATH + "/refactoring/introduceConstant/");
  }

  @Override
  protected DartIntroduceHandler createHandler() {
    return new DartIntroduceFinalVariableHandler();
  }

  public void testReplaceAll1() throws Throwable {
    doTest();
  }
}
