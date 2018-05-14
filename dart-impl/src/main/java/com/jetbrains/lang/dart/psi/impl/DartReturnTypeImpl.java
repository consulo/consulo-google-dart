// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartReturnTypeImpl extends DartPsiCompositeElementImpl implements DartReturnType {

  public DartReturnTypeImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitReturnType(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DartType getType() {
    return findChildByClass(DartType.class);
  }

}
