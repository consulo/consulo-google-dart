package com.jetbrains.lang.dart.ide.surroundWith.expression;

import consulo.google.dart.localize.DartLocalize;
import consulo.language.psi.PsiElement;

/**
 * @author: Fedor.Korotkov
 */
public class DartWithParenthesisExpressionSurrounder extends DartWithExpressionSurrounder {
    @Override
    protected String getTemplateText(PsiElement expr) {
        return "(" + expr.getText() + ")";
    }

    @Override
    public String getTemplateDescription() {
        return DartLocalize.dartSurroundWithParenthesis().get();
    }
}
