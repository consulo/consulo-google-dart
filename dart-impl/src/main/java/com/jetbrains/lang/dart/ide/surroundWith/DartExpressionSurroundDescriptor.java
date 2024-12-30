package com.jetbrains.lang.dart.ide.surroundWith;

import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.ide.surroundWith.expression.DartWithNotParenthesisExpressionSurrounder;
import com.jetbrains.lang.dart.ide.surroundWith.expression.DartWithParenthesisExpressionSurrounder;
import com.jetbrains.lang.dart.psi.DartExpression;
import com.jetbrains.lang.dart.util.DartRefactoringUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.surroundWith.SurroundDescriptor;
import consulo.language.editor.surroundWith.Surrounder;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;

import jakarta.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
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

  @Nonnull
  @Override
  public Language getLanguage() {
    return DartLanguage.INSTANCE;
  }
}
