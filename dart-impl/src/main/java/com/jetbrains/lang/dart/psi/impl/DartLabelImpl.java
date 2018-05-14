// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.Nonnull;

import com.jetbrains.lang.dart.psi.*;

public class DartLabelImpl extends AbstractDartComponentImpl implements DartLabel {

  public DartLabelImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitLabel(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public DartComponentName getComponentName() {
    return findNotNullChildByClass(DartComponentName.class);
  }

}
