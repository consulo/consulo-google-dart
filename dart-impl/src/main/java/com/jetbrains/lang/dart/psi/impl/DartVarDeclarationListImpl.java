// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartVarDeclarationListImpl extends DartPsiCompositeElementImpl implements DartVarDeclarationList {

  public DartVarDeclarationListImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitVarDeclarationList(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public DartVarAccessDeclaration getVarAccessDeclaration() {
    return findNotNullChildByClass(DartVarAccessDeclaration.class);
  }

  @Override
  @Nonnull
  public List<DartVarDeclarationListPart> getVarDeclarationListPartList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartVarDeclarationListPart.class);
  }

  @Override
  @Nullable
  public DartVarInit getVarInit() {
    return findChildByClass(DartVarInit.class);
  }

}
