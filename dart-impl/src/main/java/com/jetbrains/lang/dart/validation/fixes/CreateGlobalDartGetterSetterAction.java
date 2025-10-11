package com.jetbrains.lang.dart.validation.fixes;

import com.jetbrains.lang.dart.psi.DartExecutionScope;
import consulo.google.dart.localize.DartLocalize;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class CreateGlobalDartGetterSetterAction extends CreateDartGetterSetterAction {
  public CreateGlobalDartGetterSetterAction(@Nonnull String name, boolean isGetter) {
    super(name, isGetter, false);
  }

  @Nonnull
  @Override
  public LocalizeValue getName() {
    return myGetter ? DartLocalize.dartCreateGlobalGetterFixName(myFunctionName)
      : DartLocalize.dartCreateGlobalSetterFixName(myFunctionName);
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
