// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartRedirectionImpl extends DartPsiCompositeElementImpl implements DartRedirection {

  public DartRedirectionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitRedirection(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DartArguments getArguments() {
    return findChildByClass(DartArguments.class);
  }

  @Override
  @Nullable
  public DartReferenceExpression getReferenceExpression() {
    return findChildByClass(DartReferenceExpression.class);
  }

}
