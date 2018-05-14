// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartDefaultCaseImpl extends DartPsiCompositeElementImpl implements DartDefaultCase {

  public DartDefaultCaseImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitDefaultCase(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public List<DartExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartExpression.class);
  }

  @Override
  @Nullable
  public DartLabel getLabel() {
    return findChildByClass(DartLabel.class);
  }

  @Override
  @Nonnull
  public DartStatements getStatements() {
    return findNotNullChildByClass(DartStatements.class);
  }

}
