// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartMetadataImpl extends DartPsiCompositeElementImpl implements DartMetadata {

  public DartMetadataImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitMetadata(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DartArguments getArguments() {
    return findChildByClass(DartArguments.class);
  }

  @Override
  @Nonnull
  public DartReferenceExpression getReferenceExpression() {
    return findNotNullChildByClass(DartReferenceExpression.class);
  }

}
