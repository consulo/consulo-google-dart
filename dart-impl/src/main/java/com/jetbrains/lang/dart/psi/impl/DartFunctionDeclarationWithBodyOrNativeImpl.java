// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.*;

import com.jetbrains.lang.dart.psi.*;

public class DartFunctionDeclarationWithBodyOrNativeImpl extends AbstractDartComponentImpl implements DartFunctionDeclarationWithBodyOrNative {

  public DartFunctionDeclarationWithBodyOrNativeImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitFunctionDeclarationWithBodyOrNative(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public DartComponentName getComponentName() {
    return findNotNullChildByClass(DartComponentName.class);
  }

  @Override
  @Nonnull
  public DartFormalParameterList getFormalParameterList() {
    return findNotNullChildByClass(DartFormalParameterList.class);
  }

  @Override
  @Nullable
  public DartFunctionBody getFunctionBody() {
    return findChildByClass(DartFunctionBody.class);
  }

  @Override
  @Nonnull
  public List<DartMetadata> getMetadataList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartMetadata.class);
  }

  @Override
  @Nullable
  public DartReturnType getReturnType() {
    return findChildByClass(DartReturnType.class);
  }

  @Override
  @Nullable
  public DartStringLiteralExpression getStringLiteralExpression() {
    return findChildByClass(DartStringLiteralExpression.class);
  }

}
