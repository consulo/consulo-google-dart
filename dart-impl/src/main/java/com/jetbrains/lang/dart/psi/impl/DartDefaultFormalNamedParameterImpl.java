// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartDefaultFormalNamedParameterImpl extends DartPsiCompositeElementImpl implements DartDefaultFormalNamedParameter {

  public DartDefaultFormalNamedParameterImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitDefaultFormalNamedParameter(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DartExpression getExpression() {
    return findChildByClass(DartExpression.class);
  }

  @Override
  @Nonnull
  public DartNormalFormalParameter getNormalFormalParameter() {
    return findNotNullChildByClass(DartNormalFormalParameter.class);
  }

}
