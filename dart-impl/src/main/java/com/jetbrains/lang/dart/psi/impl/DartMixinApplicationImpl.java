// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartMixinApplicationImpl extends DartPsiCompositeElementImpl implements DartMixinApplication {

  public DartMixinApplicationImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitMixinApplication(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DartInterfaces getInterfaces() {
    return findChildByClass(DartInterfaces.class);
  }

  @Override
  @Nullable
  public DartMixins getMixins() {
    return findChildByClass(DartMixins.class);
  }

  @Override
  @Nullable
  public DartType getType() {
    return findChildByClass(DartType.class);
  }

}
