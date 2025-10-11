package com.jetbrains.lang.dart.validation.fixes;

import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartReference;
import com.jetbrains.lang.dart.util.DartPresentableUtil;
import com.jetbrains.lang.dart.util.DartResolveUtil;
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
import consulo.util.lang.StringUtil;
import jakarta.annotation.Nonnull;

abstract public class CreateVariableActionBase extends BaseCreateFix {
    private static final Logger LOG = Logger.getInstance(CreateVariableActionBase.class);
    protected final String myName;
    protected final boolean myStatic;

    public CreateVariableActionBase(String name, boolean isStatic) {
        myName = name;
        myStatic = isStatic;
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

        buildTemplate(template, psiElement);

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

    protected void buildTemplate(Template template, PsiElement element) {
        if (myStatic) {
            template.addTextSegment("static ");
        }
        DartClass dartClass = DartResolveUtil.suggestType(element);
        if (dartClass == null) {
            template.addVariable(DartPresentableUtil.getExpression("var"), true);
        }
        else {
            template.addTextSegment(StringUtil.notNullize(dartClass.getName()));
        }
        template.addTextSegment(" ");
        template.addTextSegment(myName);
        template.addTextSegment(";\n");
    }
}
