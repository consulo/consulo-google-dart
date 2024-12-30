package com.jetbrains.lang.dart.psi.impl;

import com.jetbrains.lang.dart.psi.DartEmbeddedContent;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiElementProcessor;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.psi.xml.XmlTagChild;

import jakarta.annotation.Nonnull;

public class DartEmbeddedContentImpl extends DartPsiCompositeElementImpl implements DartEmbeddedContent {
  public DartEmbeddedContentImpl(@Nonnull ASTNode node) {
    super(node);
  }

  public XmlTag getParentTag() {
    final PsiElement parent = getParent();
    if (parent instanceof XmlTag) return (XmlTag)parent;
    return null;
  }

  public XmlTagChild getNextSiblingInTag() {
    PsiElement nextSibling = getNextSibling();
    if (nextSibling instanceof XmlTagChild) return (XmlTagChild)nextSibling;
    return null;
  }

  public XmlTagChild getPrevSiblingInTag() {
    final PsiElement prevSibling = getPrevSibling();
    if (prevSibling instanceof XmlTagChild) return (XmlTagChild)prevSibling;
    return null;
  }

  public boolean processElements(PsiElementProcessor processor, PsiElement place) {
    // TODO
    return true;
  }
}
