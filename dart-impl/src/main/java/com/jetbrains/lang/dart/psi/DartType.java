// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi;

import javax.annotation.*;

import com.intellij.psi.PsiElement;

public interface DartType extends DartPsiCompositeElement {

  @Nonnull
  DartReferenceExpression getReferenceExpression();

  @Nullable
  DartTypeArguments getTypeArguments();

  @Nullable
  PsiElement resolveReference();

}
