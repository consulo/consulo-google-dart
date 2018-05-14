// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartCompareExpressionImpl extends DartOperatorExpressionImpl implements DartCompareExpression {

  public DartCompareExpressionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitCompareExpression(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DartEqualityOperator getEqualityOperator() {
    return findChildByClass(DartEqualityOperator.class);
  }

  @Override
  @Nonnull
  public List<DartExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartExpression.class);
  }

  @Override
  @Nullable
  public DartRelationalOperator getRelationalOperator() {
    return findChildByClass(DartRelationalOperator.class);
  }

}
