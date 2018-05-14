// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartClassDefinitionImpl extends AbstractDartPsiClass implements DartClassDefinition {

  public DartClassDefinitionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitClassDefinition(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DartClassBody getClassBody() {
    return findChildByClass(DartClassBody.class);
  }

  @Override
  @Nonnull
  public DartComponentName getComponentName() {
    return findNotNullChildByClass(DartComponentName.class);
  }

  @Override
  @Nullable
  public DartInterfaces getInterfaces() {
    return findChildByClass(DartInterfaces.class);
  }

  @Override
  @Nonnull
  public List<DartMetadata> getMetadataList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartMetadata.class);
  }

  @Override
  @Nullable
  public DartMixinApplication getMixinApplication() {
    return findChildByClass(DartMixinApplication.class);
  }

  @Override
  @Nullable
  public DartMixins getMixins() {
    return findChildByClass(DartMixins.class);
  }

  @Override
  @Nullable
  public DartStringLiteralExpression getStringLiteralExpression() {
    return findChildByClass(DartStringLiteralExpression.class);
  }

  @Override
  @Nullable
  public DartSuperclass getSuperclass() {
    return findChildByClass(DartSuperclass.class);
  }

  @Override
  @Nullable
  public DartTypeParameters getTypeParameters() {
    return findChildByClass(DartTypeParameters.class);
  }

}
