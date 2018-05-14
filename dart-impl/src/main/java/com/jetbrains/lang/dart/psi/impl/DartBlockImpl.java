// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartBlockImpl extends DartPsiCompositeElementImpl implements DartBlock {

  public DartBlockImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitBlock(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DartStatements getStatements() {
    return findChildByClass(DartStatements.class);
  }

}
