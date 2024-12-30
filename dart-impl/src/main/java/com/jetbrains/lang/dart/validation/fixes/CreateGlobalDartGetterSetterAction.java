package com.jetbrains.lang.dart.validation.fixes;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.psi.DartExecutionScope;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class CreateGlobalDartGetterSetterAction extends CreateDartGetterSetterAction {
  public CreateGlobalDartGetterSetterAction(@Nonnull String name, boolean isGetter) {
    super(name, isGetter, false);
  }

  @Nonnull
  @Override
  public String getName() {
    return myGetter ? DartBundle.message("dart.create.global.getter.fix.name", myFunctionName)
      : DartBundle.message("dart.create.global.setter.fix.name", myFunctionName);
  }

  @Nullable
  @Override
  protected PsiElement findAnchor(PsiElement element) {
    PsiElement scopeBody = PsiTreeUtil.getTopmostParentOfType(element, DartExecutionScope.class);
    PsiElement result = element;
    while (result.getParent() != null && result.getParent() != scopeBody) {
      result = result.getParent();
    }
    return result;
  }
}
