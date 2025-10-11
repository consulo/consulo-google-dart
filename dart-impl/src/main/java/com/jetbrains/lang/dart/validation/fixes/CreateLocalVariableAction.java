package com.jetbrains.lang.dart.validation.fixes;

import com.jetbrains.lang.dart.psi.DartStatements;
import consulo.google.dart.localize.DartLocalize;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class CreateLocalVariableAction extends CreateVariableActionBase {
  public CreateLocalVariableAction(String name) {
    super(name, false);
  }

  @Nonnull
  @Override
  public LocalizeValue getName() {
    return DartLocalize.dartCreateLocalVariable(myName);
  }

  @Nullable
  @Override
  protected PsiElement getScopeBody(PsiElement element) {
    return PsiTreeUtil.getParentOfType(element, DartStatements.class);
  }
}
