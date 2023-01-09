package com.jetbrains.lang.dart.psi;

import consulo.language.psi.PsiElement;

public class DartRecursiveVisitor extends DartVisitor {
  @Override
  public void visitElement(PsiElement element) {
    element.acceptChildren(this);
  }
}
