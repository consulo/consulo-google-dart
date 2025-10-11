package com.jetbrains.lang.dart.validation.fixes;

import com.jetbrains.lang.dart.ide.generation.DartImplementMethodHandler;
import consulo.codeEditor.Editor;
import consulo.google.dart.localize.DartLocalize;
import consulo.language.psi.PsiElement;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class ImplementMethodAction extends FixAndIntentionAction {

  private final int myOffset;

  public ImplementMethodAction(int offset) {
    myOffset = offset;
  }

  @Nonnull
  @Override
  public LocalizeValue getName() {
    return DartLocalize.dartImplementMethodsFixName();
  }

  @Override
  protected void applyFix(Project project, @Nonnull PsiElement psiElement, @Nullable Editor editor) {
    if (editor != null) {
      new DartImplementMethodHandler().invoke(project, editor, psiElement.getContainingFile(), myOffset);
    }
  }
}
