package com.jetbrains.lang.dart.generate;

import consulo.ide.impl.idea.openapi.util.io.FileUtil;
import com.jetbrains.lang.dart.ide.generation.CreateGetterSetterFix;
import com.jetbrains.lang.dart.util.DartTestUtils;
import jakarta.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
public abstract class DartGenerateActionTest extends DartGenerateActionTestBase {
  @Nonnull
  @Override
  protected String getTestDataPath() {
    return DartTestUtils.BASE_TEST_DATA_PATH +  consulo.ide.impl.idea.openapi.util.io.FileUtil.toSystemDependentName("/generate/");
  }

  public void testImplement1() throws Throwable {
    doImplementTest();
  }

  public void testImplement2() throws Throwable {
    doImplementTest();
  }

  public void testImplement3() throws Throwable {
    doImplementTest();
  }

  public void testImplement4() throws Throwable {
    doImplementTest();
  }

  public void testImplement5() throws Throwable {
    doImplementTest();
  }

  public void testImplement_WEB_2479() throws Throwable {
    doImplementTest();
  }

  public void testImplement_WEB_2479_2() throws Throwable {
    doImplementTest();
  }

  public void testImplementMixin1() throws Throwable {
    doImplementTest();
  }

  public void testOverride1() throws Throwable {
    doOverrideTest();
  }

  public void testOverride2() throws Throwable {
    doOverrideTest();
  }

  public void testOverride3() throws Throwable {
    doOverrideTest();
  }

  public void testOverride4() throws Throwable {
    doOverrideTest();
  }

  public void testOverrideMixin1() throws Throwable {
    doOverrideTest();
  }

  public void testGetter1() throws Throwable {
    doGetterSetterTest(CreateGetterSetterFix.Strategy.GETTER);
  }

  public void testGetter2() throws Throwable {
    doGetterSetterTest(CreateGetterSetterFix.Strategy.GETTER);
  }

  public void testSetter1() throws Throwable {
    doGetterSetterTest(CreateGetterSetterFix.Strategy.SETTER);
  }

  public void testGetterSetter1() throws Throwable {
    doGetterSetterTest(CreateGetterSetterFix.Strategy.GETTERSETTER);
  }

  public void testGetterSetter2() throws Throwable {
    doGetterSetterTest(CreateGetterSetterFix.Strategy.GETTERSETTER);
  }

  public void testGetterSetter3() throws Throwable {
    doGetterSetterTest(CreateGetterSetterFix.Strategy.GETTERSETTER);
  }
}
