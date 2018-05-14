// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartUserDefinableOperatorImpl extends DartPsiCompositeElementImpl implements DartUserDefinableOperator {

  public DartUserDefinableOperatorImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitUserDefinableOperator(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DartAdditiveOperator getAdditiveOperator() {
    return findChildByClass(DartAdditiveOperator.class);
  }

  @Override
  @Nullable
  public DartBitwiseOperator getBitwiseOperator() {
    return findChildByClass(DartBitwiseOperator.class);
  }

  @Override
  @Nullable
  public DartMultiplicativeOperator getMultiplicativeOperator() {
    return findChildByClass(DartMultiplicativeOperator.class);
  }

  @Override
  @Nullable
  public DartRelationalOperator getRelationalOperator() {
    return findChildByClass(DartRelationalOperator.class);
  }

  @Override
  @Nullable
  public DartShiftOperator getShiftOperator() {
    return findChildByClass(DartShiftOperator.class);
  }

}
