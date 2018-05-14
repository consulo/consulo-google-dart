// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartForLoopPartsImpl extends DartPsiCompositeElementImpl implements DartForLoopParts {

  public DartForLoopPartsImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitForLoopParts(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DartExpression getExpression() {
    return findChildByClass(DartExpression.class);
  }

  @Override
  @Nullable
  public DartExpressionList getExpressionList() {
    return findChildByClass(DartExpressionList.class);
  }

  @Override
  @Nullable
  public DartForInPart getForInPart() {
    return findChildByClass(DartForInPart.class);
  }

  @Override
  @Nullable
  public DartVarDeclarationList getVarDeclarationList() {
    return findChildByClass(DartVarDeclarationList.class);
  }

}
