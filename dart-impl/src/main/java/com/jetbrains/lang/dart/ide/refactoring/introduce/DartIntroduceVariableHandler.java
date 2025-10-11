package com.jetbrains.lang.dart.ide.refactoring.introduce;

import consulo.google.dart.localize.DartLocalize;

/**
 * @author: Fedor.Korotkov
 */
public class DartIntroduceVariableHandler extends DartIntroduceHandler {
    public DartIntroduceVariableHandler() {
        super(DartLocalize.refactoringIntroduceVariableDialogTitle());
    }

    @Override
    protected String getDeclarationString(DartIntroduceOperation operation, String initExpression) {
        return "var " + operation.getName() + " = " + initExpression + ";";
    }
}
