// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartSymbolLiteralExpressionImpl extends DartExpressionImpl implements DartSymbolLiteralExpression {

  public DartSymbolLiteralExpressionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitSymbolLiteralExpression(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DartReferenceExpression getReferenceExpression() {
    return findChildByClass(DartReferenceExpression.class);
  }

  @Override
  @Nullable
  public DartUserDefinableOperator getUserDefinableOperator() {
    return findChildByClass(DartUserDefinableOperator.class);
  }

}
