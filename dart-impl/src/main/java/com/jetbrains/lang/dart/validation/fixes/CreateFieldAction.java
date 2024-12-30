package com.jetbrains.lang.dart.validation.fixes;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartExecutionScope;
import com.jetbrains.lang.dart.psi.DartReference;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class CreateFieldAction extends CreateVariableActionBase {
  public CreateFieldAction(String name, boolean isStatic) {
    super(name, isStatic);
  }

  @Nonnull
  @Override
  public String getName() {
    return DartBundle.message("dart.create.field", myName);
  }

  @Nullable
  @Override
  protected PsiElement findAnchor(PsiElement element) {
    DartReference leftReference = DartResolveUtil.getLeftReference(PsiTreeUtil.getParentOfType(element, DartReference.class));
    return leftReference == null ? super.findAnchor(element) : DartResolveUtil.getBody(leftReference.resolveDartClass().getDartClass());
  }

  @Override
  protected PsiElement getScopeBody(PsiElement element) {
    final PsiElement classBody = DartResolveUtil.getBody(PsiTreeUtil.getParentOfType(element, DartClass.class));
    return classBody != null ? classBody : PsiTreeUtil.getTopmostParentOfType(element, DartExecutionScope.class);
  }
}
