package com.jetbrains.lang.dart.generate;

import consulo.ide.impl.idea.openapi.util.io.FileUtil;
import com.jetbrains.lang.dart.ide.generation.CreateGetterSetterFix;
import com.jetbrains.lang.dart.util.DartTestUtils;
import javax.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
public abstract class DartGenerateActionInHtmlTest extends DartGenerateActionTestBase {
  @Nonnull
  @Override
  protected String getTestDataPath() {
    return DartTestUtils.BASE_TEST_DATA_PATH + consulo.ide.impl.idea.openapi.util.io.FileUtil.toSystemDependentName("/generate/html/");
  }

  @Override
  protected void configure() {
    configureByFile(getTestName(false) + ".html");
  }

  public void testImplement3() throws Throwable {
    doImplementTest();
  }

  public void testOverride3() throws Throwable {
    doOverrideTest();
  }

  public void testGetterSetter3() throws Throwable {
    doGetterSetterTest(CreateGetterSetterFix.Strategy.GETTERSETTER);
  }
}
