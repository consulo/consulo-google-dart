// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartSwitchStatementImpl extends DartPsiCompositeElementImpl implements DartSwitchStatement {

  public DartSwitchStatementImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitSwitchStatement(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DartDefaultCase getDefaultCase() {
    return findChildByClass(DartDefaultCase.class);
  }

  @Override
  @Nullable
  public DartExpression getExpression() {
    return findChildByClass(DartExpression.class);
  }

  @Override
  @Nonnull
  public List<DartSwitchCase> getSwitchCaseList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartSwitchCase.class);
  }

}
