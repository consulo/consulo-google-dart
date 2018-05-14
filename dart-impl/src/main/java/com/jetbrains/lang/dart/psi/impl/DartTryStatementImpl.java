// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartTryStatementImpl extends DartPsiCompositeElementImpl implements DartTryStatement {

  public DartTryStatementImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitTryStatement(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DartBlock getBlock() {
    return findChildByClass(DartBlock.class);
  }

  @Override
  @Nullable
  public DartFinallyPart getFinallyPart() {
    return findChildByClass(DartFinallyPart.class);
  }

  @Override
  @Nonnull
  public List<DartOnPart> getOnPartList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartOnPart.class);
  }

}
