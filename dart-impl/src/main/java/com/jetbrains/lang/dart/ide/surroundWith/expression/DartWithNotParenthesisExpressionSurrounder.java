package com.jetbrains.lang.dart.ide.surroundWith.expression;

import consulo.google.dart.localize.DartLocalize;
import consulo.language.psi.PsiElement;
import consulo.localize.LocalizeValue;

/**
 * @author: Fedor.Korotkov
 */
public class DartWithNotParenthesisExpressionSurrounder extends DartWithExpressionSurrounder {
    @Override
    protected String getTemplateText(PsiElement expr) {
        return "!(" + expr.getText() + ")";
    }

    @Override
    public LocalizeValue getTemplateDescription() {
        return DartLocalize.dartSurroundWithNotParenthesis();
    }
}
