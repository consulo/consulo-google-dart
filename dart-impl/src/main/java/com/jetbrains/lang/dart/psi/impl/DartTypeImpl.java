// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;
import com.jetbrains.lang.dart.util.DartPsiImplUtil;

public class DartTypeImpl extends DartPsiCompositeElementImpl implements DartType {

  public DartTypeImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitType(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public DartReferenceExpression getReferenceExpression() {
    return findNotNullChildByClass(DartReferenceExpression.class);
  }

  @Override
  @Nullable
  public DartTypeArguments getTypeArguments() {
    return findChildByClass(DartTypeArguments.class);
  }

  @Nullable
  public PsiElement resolveReference() {
    return DartPsiImplUtil.resolveReference(this);
  }

}
