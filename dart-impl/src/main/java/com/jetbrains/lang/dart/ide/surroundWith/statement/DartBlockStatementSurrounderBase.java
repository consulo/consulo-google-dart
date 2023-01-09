package com.jetbrains.lang.dart.ide.surroundWith.statement;

import com.jetbrains.lang.dart.psi.DartBlock;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author: Fedor.Korotkov
 */
public abstract class DartBlockStatementSurrounderBase extends DartStatementSurrounder {
  @Override
  protected PsiElement findElementToAdd(@Nonnull PsiElement surrounder) {
    final DartBlock block = PsiTreeUtil.getChildOfType(surrounder, DartBlock.class);
    return block == null ? null : block.getStatements();
  }

  @Override
  protected int cleanUpAndGetPlaceForCaret(@Nonnull PsiElement surrounder) {
    final PsiElement childToDelete = findElementToDelete(surrounder); //true
    final int result = childToDelete == null ? surrounder.getTextOffset() : childToDelete.getTextOffset();
    if (childToDelete != null) {
      childToDelete.delete();
    }
    return result;
  }

  @Nullable
  protected abstract PsiElement findElementToDelete(PsiElement surrounder);
}
