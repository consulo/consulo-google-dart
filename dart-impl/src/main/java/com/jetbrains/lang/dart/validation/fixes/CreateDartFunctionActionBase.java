package com.jetbrains.lang.dart.validation.fixes;

import com.jetbrains.lang.dart.psi.DartArgumentList;
import com.jetbrains.lang.dart.psi.DartCallExpression;
import com.jetbrains.lang.dart.psi.DartReference;
import com.jetbrains.lang.dart.util.DartPresentableUtil;
import consulo.codeEditor.Editor;
import consulo.google.dart.localize.DartLocalize;
import consulo.language.editor.refactoring.util.CommonRefactoringUtil;
import consulo.language.editor.template.Template;
import consulo.language.editor.template.TemplateManager;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.localize.LocalizeValue;
import consulo.logging.Logger;
import consulo.project.Project;
import jakarta.annotation.Nonnull;

abstract public class CreateDartFunctionActionBase extends BaseCreateFix {
    private static final Logger LOG = Logger.getInstance(CreateDartFunctionActionBase.class);

    protected final String myFunctionName;

    public CreateDartFunctionActionBase(@Nonnull String name) {
        myFunctionName = name;
    }

    @Override
    @Nonnull
    public LocalizeValue getFamilyName() {
        return DartLocalize.dartCreateFunctionIntentionFamily();
    }

    @Override
    protected boolean isAvailable(Project project, PsiElement element, Editor editor, PsiFile file) {
        if (PsiTreeUtil.getParentOfType(myElement, DartReference.class) == null) {
            return false;
        }
        final PsiElement anchor = findAnchor(element);
        return anchor != null && !isInDartSdkOrDartPackagesFolder(anchor.getContainingFile());
    }

    @Override
    protected void applyFix(Project project, @Nonnull PsiElement psiElement, Editor editor) {
        final TemplateManager templateManager = TemplateManager.getInstance(project);
        Template template = templateManager.createTemplate("", "");
        template.setToReformat(true);

        if (!buildTemplate(template, psiElement)) {
            return;
        }

        PsiElement anchor = findAnchor(psiElement);

        if (anchor == null) {
            CommonRefactoringUtil.showErrorHint(project,
                editor,
                DartLocalize.dartCreateFunctionIntentionFamily().get(),
                DartLocalize.dartCannotFindPlaceToCreate().get(),
                null);
            return;
        }

        final Editor openedEditor = navigate(project, anchor.getTextOffset(), anchor.getContainingFile().getVirtualFile());
        if (openedEditor != null) {
            templateManager.startTemplate(openedEditor, template);
        }
    }

    protected boolean buildTemplate(Template template, PsiElement psiElement) {
        DartCallExpression callExpression = PsiTreeUtil.getParentOfType(psiElement, DartCallExpression.class);
        if (callExpression == null) {
            LOG.debug(getName() + " cannot find function call for: " + psiElement.getText());
            return false;
        }

        buildFunctionText(template, callExpression);
        return true;
    }

    protected void buildFunctionText(Template template, @Nonnull DartCallExpression callExpression) {
        template.addTextSegment(myFunctionName);
        template.addTextSegment("(");
        buildParameters(template, callExpression);
        template.addTextSegment("){\n");
        template.addEndVariable();
        template.addTextSegment("\n}\n");
    }

    private void buildParameters(Template template, DartCallExpression callExpression) {
        DartArgumentList argumentList = callExpression.getArguments().getArgumentList();
        if (argumentList != null) {
            DartPresentableUtil.appendArgumentList(template, argumentList);
        }
    }
}
