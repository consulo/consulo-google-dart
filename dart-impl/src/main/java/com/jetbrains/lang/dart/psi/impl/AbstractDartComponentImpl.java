package com.jetbrains.lang.dart.psi.impl;

import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.DartTokenTypes;
import com.jetbrains.lang.dart.psi.*;
import com.jetbrains.lang.dart.util.DartPresentableUtil;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.access.RequiredWriteAction;
import consulo.language.ast.ASTNode;
import consulo.language.icon.IconDescriptorUpdaters;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.localize.LocalizeValue;
import consulo.navigation.ItemPresentation;
import consulo.ui.image.Image;
import consulo.language.util.IncorrectOperationException;
import consulo.util.lang.StringUtil;

import jakarta.annotation.Nonnull;
import org.jspecify.annotations.Nullable;

abstract public class AbstractDartComponentImpl extends DartPsiCompositeElementImpl implements DartComponent {
    public AbstractDartComponentImpl(@Nonnull ASTNode node) {
        super(node);
    }

    @Override
    @RequiredReadAction
    public String getName() {
        DartComponentName name = getComponentName();
        if (name != null) {
            return name.getText();
        }
        return super.getName();
    }

    @Override
    @RequiredWriteAction
    public PsiElement setName(@Nonnull String name) throws IncorrectOperationException {
        DartComponentName componentName = getComponentName();
        if (componentName != null) {
            componentName.setName(name);
        }
        return this;
    }

    @Override
    @RequiredReadAction
    public @Nullable PsiElement getNameIdentifier() {
        return getComponentName();
    }

    @Override
    @RequiredReadAction
    public boolean isStatic() {
        return findChildByType(DartTokenTypes.STATIC) != null;
    }

    @Override
    @RequiredReadAction
    public boolean isPublic() {
        String name = getName();
        return name != null && !name.startsWith("_");
    }

    @Override
    public boolean isConstructor() {
        return DartComponentType.typeOf(this) == DartComponentType.CONSTRUCTOR;
    }

    @Override
    @RequiredReadAction
    public boolean isSetter() {
        return findChildByType(DartTokenTypes.SET) != null;
    }

    @Override
    @RequiredReadAction
    public boolean isGetter() {
        return findChildByType(DartTokenTypes.GET) != null;
    }

    @Override
    @RequiredReadAction
    public boolean isAbstract() {
        DartComponentType componentType = DartComponentType.typeOf(this);
        return componentType == DartComponentType.CLASS && findChildByType(DartTokenTypes.ABSTRACT) != null
            || componentType == DartComponentType.METHOD && findChildByType(DartTokenTypes.FUNCTION_BODY) == null;
    }

    @Override
    @RequiredReadAction
    public @Nullable DartMetadata getMetadataByName(String name) {
        for (DartMetadata metadata : PsiTreeUtil.getChildrenOfTypeAsList(this, DartMetadata.class)) {
            if ("deprecated".equals(metadata.getReferenceExpression().getText())) {
                return metadata;
            }
        }
        return null;
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            @Override
            @RequiredReadAction
            public LocalizeValue getPresentableText() {
                StringBuilder result = new StringBuilder();
                result.append(getComponentName());
                DartComponentType type = DartComponentType.typeOf(AbstractDartComponentImpl.this);
                if ((type == DartComponentType.METHOD || type == DartComponentType.FUNCTION || type == DartComponentType.CONSTRUCTOR)
                    && !(isGetter() || isSetter())) {
                    String parameterList = DartPresentableUtil.getPresentableParameterList(AbstractDartComponentImpl.this);
                    result.append("(").append(parameterList).append(")");
                }
                if (type == DartComponentType.METHOD || type == DartComponentType.FIELD || type == DartComponentType.FUNCTION) {
                    DartReturnType returnType = PsiTreeUtil.getChildOfType(AbstractDartComponentImpl.this, DartReturnType.class);
                    DartType dartType = PsiTreeUtil.getChildOfType(AbstractDartComponentImpl.this, DartType.class);
                    if (returnType != null) {
                        result.append(":");
                        result.append(DartPresentableUtil.buildTypeText(AbstractDartComponentImpl.this, returnType, null));
                    }
                    else if (dartType != null) {
                        result.append(":");
                        result.append(DartPresentableUtil.buildTypeText(AbstractDartComponentImpl.this, dartType, null));
                    }
                }
                return LocalizeValue.of(result.toString());
            }

            @Nullable
            @RequiredReadAction
            private String getComponentName() {
                String name = getName();
                if (DartComponentType.typeOf(AbstractDartComponentImpl.this) == DartComponentType.CONSTRUCTOR) {
                    DartClass dartClass = PsiTreeUtil.getParentOfType(AbstractDartComponentImpl.this, DartClass.class);
                    if (dartClass == null) {
                        return name;
                    }
                    return StringUtil.isEmpty(name) ? dartClass.getName() : dartClass.getName() + "." + name;
                }
                return name;
            }

            @Override
            @RequiredReadAction
            public String getLocationString() {
                if (!isValid()) {
                    return "";
                }
                if (!(AbstractDartComponentImpl.this instanceof DartClass)) {
                    DartClass dartClass = PsiTreeUtil.getParentOfType(AbstractDartComponentImpl.this, DartClass.class);
                    if (dartClass != null) {
                        return dartClass.getName();
                    }
                }
                DartExecutionScope root = PsiTreeUtil.getTopmostParentOfType(AbstractDartComponentImpl.this, DartExecutionScope.class);
                DartPartOfStatement partOfStatement = PsiTreeUtil.getChildOfType(root, DartPartOfStatement.class);
                return partOfStatement == null ? null : partOfStatement.getLibraryName();
            }

            @Override
            @RequiredReadAction
            public @Nullable Image getIcon() {
                return IconDescriptorUpdaters.getIcon(AbstractDartComponentImpl.this, 0);
            }
        };
    }
}
