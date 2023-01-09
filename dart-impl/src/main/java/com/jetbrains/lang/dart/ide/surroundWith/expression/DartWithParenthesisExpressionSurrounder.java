package com.jetbrains.lang.dart.ide.surroundWith.expression;

import com.jetbrains.lang.dart.DartBundle;
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
    return DartBundle.message("dart.surround.with.parenthesis");
  }
}
