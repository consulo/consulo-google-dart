package com.jetbrains.lang.dart.validation.fixes;

import javax.annotation.Nonnull;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.psi.DartExecutionScope;

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
