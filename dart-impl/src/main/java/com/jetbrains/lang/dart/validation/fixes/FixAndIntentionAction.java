package com.jetbrains.lang.dart.validation.fixes;

import consulo.codeEditor.Editor;
import consulo.language.editor.FileModificationService;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemDescriptor;
import consulo.language.editor.intention.SyntheticIntentionAction;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.util.IncorrectOperationException;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public abstract class FixAndIntentionAction implements LocalQuickFix, SyntheticIntentionAction {
  @Nullable
  protected PsiElement myElement = null;

  public void applyFix(@Nonnull Project project, @Nonnull ProblemDescriptor descriptor) {
    applyFix(project, descriptor.getPsiElement(), null);
  }

  @Nonnull
  public LocalizeValue getFamilyName() {
    return getName();
  }

  @Nonnull
  public LocalizeValue getText() {
    return getName();
  }

  public boolean startInWriteAction() {
    return false;
  }

  public void setElement(@Nullable PsiElement element) {
    myElement = element;
  }

  public boolean isAvailable(@Nonnull Project project, Editor editor, PsiFile file) {
    if (myElement == null) return false;
    return isAvailable(project, myElement, editor, file);
  }

  protected boolean isAvailable(Project project, @Nullable PsiElement element, Editor editor, PsiFile file) {
    return true;
  }

  public void invoke(@Nonnull final Project project, final Editor editor, final PsiFile file) throws IncorrectOperationException {
    if (!FileModificationService.getInstance().prepareFileForWrite(file)) return;
    if (myElement == null) return;
    applyFix(project, myElement, editor);
  }

  protected abstract void applyFix(Project project, @Nonnull PsiElement psiElement, @Nullable Editor editor);
}