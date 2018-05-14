package com.jetbrains.lang.dart.ide.surroundWith;

import javax.annotation.Nonnull;

import com.intellij.lang.surroundWith.SurroundDescriptor;
import com.intellij.lang.surroundWith.Surrounder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.jetbrains.lang.dart.ide.surroundWith.expression.DartWithNotParenthesisExpressionSurrounder;
import com.jetbrains.lang.dart.ide.surroundWith.expression.DartWithParenthesisExpressionSurrounder;
import com.jetbrains.lang.dart.psi.DartExpression;
import com.jetbrains.lang.dart.util.DartRefactoringUtil;

/**
 * @author: Fedor.Korotkov
 */
public class DartExpressionSurroundDescriptor implements SurroundDescriptor {
  @Nonnull
  @Override
  public PsiElement[] getElementsToSurround(PsiFile file, int startOffset, int endOffset) {
    final DartExpression result = DartRefactoringUtil.findExpressionInRange(file, startOffset, endOffset);
    return result == null ? PsiElement.EMPTY_ARRAY : new PsiElement[]{result};
  }

  @Nonnull
  @Override
  public Surrounder[] getSurrounders() {
    return new Surrounder[]{
      new DartWithParenthesisExpressionSurrounder(),
      new DartWithNotParenthesisExpressionSurrounder()
    };
  }

  @Override
  public boolean isExclusive() {
    return false;
  }
}
