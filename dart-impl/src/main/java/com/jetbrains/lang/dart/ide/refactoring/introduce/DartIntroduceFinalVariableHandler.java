package com.jetbrains.lang.dart.ide.refactoring.introduce;

import consulo.google.dart.localize.DartLocalize;

/**
 * @author Fedor.Korotkov
 */
public class DartIntroduceFinalVariableHandler extends DartIntroduceHandler {
  public DartIntroduceFinalVariableHandler() {
    super(DartLocalize.refactoringIntroduceFinalVariableDialogTitle());
  }

  @Override
  protected String getDeclarationString(DartIntroduceOperation operation, String initExpression) {
    // todo: check if operation.getInitializer() is constant
    return "final " + operation.getName() + " = " + initExpression + ";";
  }
}
