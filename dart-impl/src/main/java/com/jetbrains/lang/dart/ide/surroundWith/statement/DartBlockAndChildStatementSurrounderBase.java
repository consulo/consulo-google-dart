package com.jetbrains.lang.dart.ide.surroundWith.statement;

import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;

import javax.annotation.Nullable;

/**
 * @author: Fedor.Korotkov
 */
public abstract class DartBlockAndChildStatementSurrounderBase<T extends PsiElement> extends DartBlockStatementSurrounderBase {
  @Nullable
  protected PsiElement findElementToDelete(PsiElement surrounder) {
    return PsiTreeUtil.getChildOfType(surrounder, getClassToDelete());
  }

  protected abstract Class<T> getClassToDelete();
}
