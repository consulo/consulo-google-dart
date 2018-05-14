// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.Nonnull;

import com.jetbrains.lang.dart.psi.*;

public class DartSuffixExpressionImpl extends DartOperatorExpressionImpl implements DartSuffixExpression {

  public DartSuffixExpressionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitSuffixExpression(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public DartExpression getExpression() {
    return findNotNullChildByClass(DartExpression.class);
  }

}
