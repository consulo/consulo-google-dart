package com.jetbrains.lang.dart.psi.impl;

import com.jetbrains.lang.dart.psi.DartDocComment;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.impl.psi.ASTWrapperPsiElement;
import consulo.language.psi.PsiElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DartDocCommentImpl extends ASTWrapperPsiElement implements DartDocComment {

  public DartDocCommentImpl(@Nonnull final ASTNode node) {
    super(node);
  }

  @Nullable
  public PsiElement getOwner() {
    return null; // todo
  }

  public IElementType getTokenType() {
    return getNode().getElementType();
  }
}
