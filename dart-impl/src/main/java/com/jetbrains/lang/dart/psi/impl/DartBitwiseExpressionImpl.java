// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.Nonnull;

import com.jetbrains.lang.dart.psi.*;

public class DartBitwiseExpressionImpl extends DartOperatorExpressionImpl implements DartBitwiseExpression {

  public DartBitwiseExpressionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitBitwiseExpression(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public DartBitwiseOperator getBitwiseOperator() {
    return findNotNullChildByClass(DartBitwiseOperator.class);
  }

  @Override
  @Nonnull
  public List<DartExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartExpression.class);
  }

}
