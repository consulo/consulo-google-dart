// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartListLiteralExpressionImpl extends DartClassReferenceImpl implements DartListLiteralExpression {

  public DartListLiteralExpressionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitListLiteralExpression(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DartExpressionList getExpressionList() {
    return findChildByClass(DartExpressionList.class);
  }

  @Override
  @Nullable
  public DartTypeArguments getTypeArguments() {
    return findChildByClass(DartTypeArguments.class);
  }

}
