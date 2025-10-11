package com.jetbrains.lang.dart.ide.generation;

import consulo.google.dart.localize.DartLocalize;
import consulo.localize.LocalizeValue;

/**
 * @author Fedor.Korotkov
 */
public class DartGenerateSetterAction extends BaseDartGenerateAction {
  @Override
  protected BaseDartGenerateHandler getGenerateHandler() {
    return new DartGenerateAccessorHandler(CreateGetterSetterFix.Strategy.SETTER) {
      @Override
      protected LocalizeValue getTitle() {
        return DartLocalize.fieldsToGenerateSetters();
      }
    };
  }
}
