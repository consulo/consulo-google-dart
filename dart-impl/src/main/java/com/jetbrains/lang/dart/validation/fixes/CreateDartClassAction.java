package com.jetbrains.lang.dart.validation.fixes;

import com.jetbrains.lang.dart.psi.DartExecutionScope;
import com.jetbrains.lang.dart.util.DartPresentableUtil;
import consulo.codeEditor.Editor;
import consulo.google.dart.localize.DartLocalize;
import consulo.language.editor.template.Template;
import consulo.language.editor.template.TemplateManager;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import jakarta.annotation.Nonnull;

public class CreateDartClassAction extends BaseCreateFix {
  public final String myClassName;

  public CreateDartClassAction(String name) {
    myClassName = name;
  }

  @Nonnull
  @Override
  public LocalizeValue getName() {
    return DartLocalize.dartCreatClassFixName(myClassName);
  }

  @Override
  protected PsiElement getScopeBody(PsiElement element) {
    return PsiTreeUtil.getTopmostParentOfType(element, DartExecutionScope.class);
  }

  @Override
  protected void applyFix(Project project, @Nonnull PsiElement psiElement, Editor editor) {
    PsiElement anchor = findAnchor(psiElement);
    if (anchor == null) {
      return;
    }

    final TemplateManager templateManager = TemplateManager.getInstance(project);
    Template template = templateManager.createTemplate("", "");
    template.setToReformat(true);

    template.addTextSegment("class ");
    template.addVariable(DartPresentableUtil.getExpression(myClassName), false);
    template.addTextSegment("{\n");
    template.addEndVariable();
    template.addTextSegment("\n}\n");

    final Editor openedEditor = navigate(project, anchor.getTextOffset(), anchor.getContainingFile().getVirtualFile());
    if (openedEditor != null) {
      templateManager.startTemplate(openedEditor, template);
    }
  }
}
