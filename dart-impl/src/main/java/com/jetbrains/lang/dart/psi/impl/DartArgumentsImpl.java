// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartArgumentsImpl extends DartPsiCompositeElementImpl implements DartArguments {

  public DartArgumentsImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitArguments(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DartArgumentList getArgumentList() {
    return findChildByClass(DartArgumentList.class);
  }

}
