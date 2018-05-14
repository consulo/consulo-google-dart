package com.jetbrains.lang.dart.ide;

import javax.annotation.Nonnull;

import com.intellij.codeInsight.hint.ImplementationTextSelectioner;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.jetbrains.lang.dart.psi.DartComponentName;

/**
 * @author: Fedor.Korotkov
 */
public class DartImplementationTextSelectioner implements ImplementationTextSelectioner {
  @Override
  public int getTextStartOffset(@Nonnull PsiElement element) {
    if (element instanceof DartComponentName) {
      element = element.getParent();
    }
    final TextRange textRange = element.getTextRange();
    return textRange.getStartOffset();
  }

  @Override
  public int getTextEndOffset(@Nonnull PsiElement element) {
    if (element instanceof DartComponentName) {
      element = element.getParent();
    }
    final TextRange textRange = element.getTextRange();
    return textRange.getEndOffset();
  }
}
