// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartPrefixExpressionImpl extends DartOperatorExpressionImpl implements DartPrefixExpression {

  public DartPrefixExpressionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitPrefixExpression(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DartPrefixOperator getPrefixOperator() {
    return findChildByClass(DartPrefixOperator.class);
  }

}
