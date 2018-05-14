// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.Nonnull;

import com.jetbrains.lang.dart.psi.*;

public class DartCatchPartImpl extends DartPsiCompositeElementImpl implements DartCatchPart {

  public DartCatchPartImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitCatchPart(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public List<DartComponentName> getComponentNameList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartComponentName.class);
  }

}
