package com.jetbrains.lang.dart.ide.generation;

import consulo.google.dart.localize.DartLocalize;
import consulo.localize.LocalizeValue;

/**
 * @author Fedor.Korotkov
 */
public class DartGenerateGetterAction extends BaseDartGenerateAction {
  @Override
  protected BaseDartGenerateHandler getGenerateHandler() {
    return new DartGenerateAccessorHandler(CreateGetterSetterFix.Strategy.GETTER) {
      @Override
      protected LocalizeValue getTitle() {
        return DartLocalize.fieldsToGenerateGetters();
      }
    };
  }
}
