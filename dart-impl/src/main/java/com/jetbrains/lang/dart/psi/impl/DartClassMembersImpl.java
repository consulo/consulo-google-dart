// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.Nonnull;

import com.jetbrains.lang.dart.psi.*;

public class DartClassMembersImpl extends DartPsiCompositeElementImpl implements DartClassMembers {

  public DartClassMembersImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitClassMembers(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public List<DartFactoryConstructorDeclaration> getFactoryConstructorDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartFactoryConstructorDeclaration.class);
  }

  @Override
  @Nonnull
  public List<DartGetterDeclaration> getGetterDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartGetterDeclaration.class);
  }

  @Override
  @Nonnull
  public List<DartIncompleteDeclaration> getIncompleteDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartIncompleteDeclaration.class);
  }

  @Override
  @Nonnull
  public List<DartMethodDeclaration> getMethodDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartMethodDeclaration.class);
  }

  @Override
  @Nonnull
  public List<DartNamedConstructorDeclaration> getNamedConstructorDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartNamedConstructorDeclaration.class);
  }

  @Override
  @Nonnull
  public List<DartOperatorDeclaration> getOperatorDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartOperatorDeclaration.class);
  }

  @Override
  @Nonnull
  public List<DartSetterDeclaration> getSetterDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartSetterDeclaration.class);
  }

  @Override
  @Nonnull
  public List<DartVarDeclarationList> getVarDeclarationListList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartVarDeclarationList.class);
  }

}
