// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartClassBodyImpl extends DartPsiCompositeElementImpl implements DartClassBody {

  public DartClassBodyImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitClassBody(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DartClassMembers getClassMembers() {
    return findChildByClass(DartClassMembers.class);
  }

}
