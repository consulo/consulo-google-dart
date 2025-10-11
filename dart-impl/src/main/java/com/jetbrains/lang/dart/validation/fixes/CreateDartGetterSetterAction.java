package com.jetbrains.lang.dart.validation.fixes;

import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartExecutionScope;
import com.jetbrains.lang.dart.psi.DartReference;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import consulo.codeEditor.Editor;
import consulo.google.dart.localize.DartLocalize;
import consulo.language.editor.template.Template;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class CreateDartGetterSetterAction extends CreateDartFunctionActionBase {

    protected final boolean myStatic;
    protected final boolean myGetter;

    public CreateDartGetterSetterAction(@Nonnull String name, boolean isGetter, boolean isStatic) {
        super(name);
        myGetter = isGetter;
        myStatic = isStatic;
    }

    @Nonnull
    @Override
    public LocalizeValue getName() {
        if (myGetter) {
            return myStatic ? DartLocalize.dartCreateStaticGetterFixName(myFunctionName)
                : DartLocalize.dartCreateGetterFixName(myFunctionName);
        }
        return myStatic ? DartLocalize.dartCreateStaticSetterFixName(myFunctionName)
            : DartLocalize.dartCreateSetterFixName(myFunctionName);
    }

    @Override
    protected boolean isAvailable(Project project, PsiElement element, Editor editor, PsiFile file) {
        return PsiTreeUtil.getParentOfType(myElement, DartReference.class) != null;
    }

    @Override
    protected boolean buildTemplate(Template template, @Nonnull PsiElement element) {
        if (myStatic) {
            template.addTextSegment("static ");
        }
        template.addTextSegment(myGetter ? "get " : "set ");
        template.addTextSegment(myFunctionName);
        if (myGetter) {
            template.addTextSegment(" => ");
            template.addEndVariable();
            template.addTextSegment(";\n");
            return true;
        }
        template.addTextSegment("(");

        DartClass dartClass = DartResolveUtil.suggestType(element);
        if (dartClass != null) {
            template.addTextSegment(dartClass.getName());
            template.addTextSegment(" ");
        }
        template.addTextSegment("value");

        template.addTextSegment("){\n");
        template.addEndVariable();
        template.addTextSegment("\n}\n");
        return true;
    }

    @Override
    protected PsiElement getScopeBody(PsiElement element) {
        final PsiElement classBody = DartResolveUtil.getBody(PsiTreeUtil.getParentOfType(element, DartClass.class));
        return classBody != null ? classBody : PsiTreeUtil.getTopmostParentOfType(element, DartExecutionScope.class);
    }

    @Nullable
    @Override
    protected PsiElement findAnchor(PsiElement element) {
        DartReference leftReference = DartResolveUtil.getLeftReference(PsiTreeUtil.getParentOfType(element, DartReference.class));
        return leftReference == null ? super.findAnchor(element) : DartResolveUtil.getBody(leftReference.resolveDartClass().getDartClass());
    }
}
