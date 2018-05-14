// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartOnPartImpl extends DartPsiCompositeElementImpl implements DartOnPart {

  public DartOnPartImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitOnPart(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public DartBlock getBlock() {
    return findNotNullChildByClass(DartBlock.class);
  }

  @Override
  @Nullable
  public DartCatchPart getCatchPart() {
    return findChildByClass(DartCatchPart.class);
  }

  @Override
  @Nullable
  public DartType getType() {
    return findChildByClass(DartType.class);
  }

}
