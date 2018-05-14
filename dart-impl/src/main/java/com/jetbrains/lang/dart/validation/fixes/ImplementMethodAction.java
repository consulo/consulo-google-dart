package com.jetbrains.lang.dart.validation.fixes;

import javax.annotation.Nonnull;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.ide.generation.DartImplementMethodHandler;

import javax.annotation.Nullable;

public class ImplementMethodAction extends FixAndIntentionAction {

  private final int myOffset;

  public ImplementMethodAction(int offset) {
    myOffset = offset;
  }

  @Nonnull
  @Override
  public String getName() {
    return DartBundle.message("dart.implement.methods.fix.name");
  }

  @Override
  protected void applyFix(Project project, @Nonnull PsiElement psiElement, @Nullable Editor editor) {
    if (editor != null) {
      new DartImplementMethodHandler().invoke(project, editor, psiElement.getContainingFile(), myOffset);
    }
  }
}
