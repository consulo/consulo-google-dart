// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.Nonnull;

import com.jetbrains.lang.dart.psi.*;

public class DartIsExpressionImpl extends DartExpressionImpl implements DartIsExpression {

  public DartIsExpressionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitIsExpression(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public DartExpression getExpression() {
    return findNotNullChildByClass(DartExpression.class);
  }

  @Override
  @Nonnull
  public DartType getType() {
    return findNotNullChildByClass(DartType.class);
  }

}
