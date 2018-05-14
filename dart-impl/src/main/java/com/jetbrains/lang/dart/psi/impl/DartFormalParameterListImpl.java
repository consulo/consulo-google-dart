// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartFormalParameterListImpl extends DartPsiCompositeElementImpl implements DartFormalParameterList {

  public DartFormalParameterListImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitFormalParameterList(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DartNamedFormalParameters getNamedFormalParameters() {
    return findChildByClass(DartNamedFormalParameters.class);
  }

  @Override
  @Nonnull
  public List<DartNormalFormalParameter> getNormalFormalParameterList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartNormalFormalParameter.class);
  }

}
