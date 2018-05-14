// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.Nonnull;

import com.jetbrains.lang.dart.psi.*;

public class DartFunctionExpressionImpl extends DartExpressionImpl implements DartFunctionExpression {

  public DartFunctionExpressionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitFunctionExpression(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public DartFormalParameterList getFormalParameterList() {
    return findNotNullChildByClass(DartFormalParameterList.class);
  }

  @Override
  @Nonnull
  public DartFunctionExpressionBody getFunctionExpressionBody() {
    return findNotNullChildByClass(DartFunctionExpressionBody.class);
  }

}
