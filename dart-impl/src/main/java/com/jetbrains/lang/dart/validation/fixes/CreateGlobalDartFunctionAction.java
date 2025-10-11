package com.jetbrains.lang.dart.validation.fixes;

import com.jetbrains.lang.dart.psi.DartExecutionScope;
import consulo.google.dart.localize.DartLocalize;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;

public class CreateGlobalDartFunctionAction extends CreateDartFunctionActionBase {
  public CreateGlobalDartFunctionAction(@Nonnull String name) {
    super(name);
  }

  @Nonnull
  @Override
  public LocalizeValue getName() {
    return DartLocalize.dartCreateGlobalFunctionFixName(myFunctionName);
  }

  @Override
  protected PsiElement getScopeBody(PsiElement element) {
    return PsiTreeUtil.getTopmostParentOfType(element, DartExecutionScope.class);
  }
}
