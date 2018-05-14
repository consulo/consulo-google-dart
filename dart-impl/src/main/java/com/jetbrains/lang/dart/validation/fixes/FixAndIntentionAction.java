package com.jetbrains.lang.dart.validation.fixes;

import javax.annotation.Nonnull;

import com.intellij.codeInsight.FileModificationService;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;

import javax.annotation.Nullable;

public abstract class FixAndIntentionAction implements LocalQuickFix, IntentionAction {
  @Nullable
  protected PsiElement myElement = null;

  public void applyFix(@Nonnull Project project, @Nonnull ProblemDescriptor descriptor) {
    applyFix(project, descriptor.getPsiElement(), null);
  }

  @Nonnull
  public String getFamilyName() {
    return getName();
  }

  @Nonnull
  public String getText() {
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