// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.Nonnull;

import com.jetbrains.lang.dart.psi.*;

public class DartShiftRightOperatorImpl extends DartPsiCompositeElementImpl implements DartShiftRightOperator {

  public DartShiftRightOperatorImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitShiftRightOperator(this);
    else super.accept(visitor);
  }

}
