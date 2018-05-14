// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.Nonnull;

import com.jetbrains.lang.dart.psi.*;

public class DartNamedFormalParametersImpl extends DartPsiCompositeElementImpl implements DartNamedFormalParameters {

  public DartNamedFormalParametersImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitNamedFormalParameters(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public List<DartDefaultFormalNamedParameter> getDefaultFormalNamedParameterList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartDefaultFormalNamedParameter.class);
  }

}
