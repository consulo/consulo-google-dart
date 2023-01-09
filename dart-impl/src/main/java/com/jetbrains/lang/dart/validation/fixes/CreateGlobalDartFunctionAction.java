package com.jetbrains.lang.dart.validation.fixes;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.psi.DartExecutionScope;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;

import javax.annotation.Nonnull;

public class CreateGlobalDartFunctionAction extends CreateDartFunctionActionBase {
  public CreateGlobalDartFunctionAction(@Nonnull String name) {
    super(name);
  }

  @Nonnull
  @Override
  public String getName() {
    return DartBundle.message("dart.create.global.function.fix.name", myFunctionName);
  }

  @Override
  protected PsiElement getScopeBody(PsiElement element) {
    return PsiTreeUtil.getTopmostParentOfType(element, DartExecutionScope.class);
  }
}
