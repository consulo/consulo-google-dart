package com.jetbrains.lang.dart.validation.fixes;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.psi.DartExecutionScope;
import com.jetbrains.lang.dart.util.DartPresentableUtil;
import consulo.codeEditor.Editor;
import consulo.language.editor.template.Template;
import consulo.language.editor.template.TemplateManager;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.project.Project;

import jakarta.annotation.Nonnull;

public class CreateDartClassAction extends BaseCreateFix {
  public final String myClassName;

  public CreateDartClassAction(String name) {
    myClassName = name;
  }

  @Nonnull
  @Override
  public String getName() {
    return DartBundle.message("dart.creat.class.fix.name", myClassName);
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
