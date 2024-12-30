package com.jetbrains.lang.dart.psi.impl;

import com.jetbrains.lang.dart.psi.DartType;
import consulo.language.ast.ASTNode;
import consulo.language.psi.util.PsiTreeUtil;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author: Fedor.Korotkov
 */
abstract public class TypedefDartPsiClass extends AbstractDartPsiClass {
  public TypedefDartPsiClass(@Nonnull ASTNode node) {
    super(node);
  }

  @Nullable
  @Override
  public DartType getSuperClass() {
    return PsiTreeUtil.getChildOfType(this, DartType.class);
  }
}
