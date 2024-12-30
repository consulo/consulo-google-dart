package com.jetbrains.lang.dart.validation.fixes;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartReference;
import com.jetbrains.lang.dart.psi.impl.DartOperatorExpressionImpl;
import com.jetbrains.lang.dart.util.DartPresentableUtil;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import consulo.codeEditor.Editor;
import consulo.language.editor.template.Template;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.project.Project;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class CreateDartOperatorAction extends CreateDartMethodAction {
  public CreateDartOperatorAction(@Nonnull String operator) {
    super(operator, false);
  }

  @Nonnull
  @Override
  public String getName() {
    return DartBundle.message("dart.create.operator.fix.name", myFunctionName);
  }

  @Override
  protected boolean isAvailable(Project project, PsiElement element, Editor editor, PsiFile file) {
    DartOperatorExpressionImpl operatorExpression = PsiTreeUtil.getParentOfType(element, DartOperatorExpressionImpl.class);
    final DartReference[] references = PsiTreeUtil.getChildrenOfType(operatorExpression, DartReference.class);
    return references != null && references[0].resolveDartClass().getDartClass() != null;
  }

  @Nullable
  @Override
  protected PsiElement findAnchor(PsiElement element) {
    DartOperatorExpressionImpl operatorExpression = PsiTreeUtil.getParentOfType(element, DartOperatorExpressionImpl.class);
    final DartReference[] references = PsiTreeUtil.getChildrenOfType(operatorExpression, DartReference.class);
    assert references != null;
    DartClass aClass = references[0].resolveDartClass().getDartClass();
    return DartResolveUtil.getBody(aClass);
  }

  @Override
  protected boolean buildTemplate(Template template, PsiElement psiElement) {
    DartOperatorExpressionImpl operatorExpression = PsiTreeUtil.getParentOfType(psiElement, DartOperatorExpressionImpl.class);
    final DartReference[] references = PsiTreeUtil.getChildrenOfType(operatorExpression, DartReference.class);
    assert references != null;
    DartClass aClass = references[0].resolveDartClass().getDartClass();
    assert aClass != null;

    template.addVariable(DartPresentableUtil.getExpression(aClass.getName()), true);
    template.addTextSegment(" ");
    template.addTextSegment("operator");
    template.addTextSegment(" ");
    template.addTextSegment(myFunctionName);
    template.addTextSegment("(");
    if (references.length > 1) {
      DartClass otherClass = references[1].resolveDartClass().getDartClass();
      if (otherClass != null) {
        template.addTextSegment(otherClass.getName());
        template.addTextSegment(" ");
      }
      template.addVariable(DartPresentableUtil.getExpression("other"), true);
    }
    template.addTextSegment("){\n");
    template.addEndVariable();
    template.addTextSegment("\n}\n");
    return true;
  }
}
