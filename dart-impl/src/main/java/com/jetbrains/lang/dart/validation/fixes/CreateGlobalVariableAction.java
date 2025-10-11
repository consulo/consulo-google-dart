package com.jetbrains.lang.dart.validation.fixes;

import com.jetbrains.lang.dart.psi.DartExecutionScope;
import consulo.google.dart.localize.DartLocalize;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class CreateGlobalVariableAction extends CreateVariableActionBase {
  public CreateGlobalVariableAction(String name) {
    super(name, false);
  }

  @Nonnull
  @Override
  public LocalizeValue getName() {
    return DartLocalize.dartCreateGlobalVariable(myName);
  }

  @Nullable
  @Override
  protected PsiElement getScopeBody(PsiElement element) {
    return PsiTreeUtil.getTopmostParentOfType(element, DartExecutionScope.class);
  }
}
