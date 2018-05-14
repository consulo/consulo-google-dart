// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartForInPartImpl extends DartPsiCompositeElementImpl implements DartForInPart {

  public DartForInPartImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitForInPart(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DartComponentName getComponentName() {
    return findChildByClass(DartComponentName.class);
  }

  @Override
  @Nonnull
  public DartExpression getExpression() {
    return findNotNullChildByClass(DartExpression.class);
  }

  @Override
  @Nullable
  public DartVarAccessDeclaration getVarAccessDeclaration() {
    return findChildByClass(DartVarAccessDeclaration.class);
  }

}
