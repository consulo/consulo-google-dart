package com.jetbrains.lang.dart.ide;

import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.psi.*;
import com.jetbrains.lang.dart.util.DartPresentableUtil;
import consulo.language.editor.completion.lookup.InsertionContext;
import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.completion.lookup.LookupElementPresentation;
import consulo.language.editor.completion.lookup.ParenthesesInsertHandler;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.localize.LocalizeValue;
import consulo.navigation.ItemPresentation;
import consulo.util.lang.StringUtil;

import jakarta.annotation.Nonnull;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Fedor.Korotkov
 */
public class DartLookupElement extends LookupElement {
    private final DartComponentName myComponentName;
    private final boolean myConstructorCompletion;

    public static Collection<DartLookupElement> convert(
        @Nonnull Collection<DartComponentName> componentNames,
        boolean constructorCompletion
    ) {
        Map<String, DartLookupElement> result = new HashMap<>();
        for (DartComponentName componentName : componentNames) {
            DartLookupElement lookupElement = new DartLookupElement(componentName, constructorCompletion);
            result.put(lookupElement.getLookupString(), lookupElement);
        }
        return result.values();
    }

    public DartLookupElement(DartComponentName name, boolean constructorCompletion) {
        myComponentName = name;
        myConstructorCompletion = constructorCompletion;
    }

    @Nonnull
    @Override
    public String getLookupString() {
        boolean completeConstructor = isCompleteConstructorComponent();
        String result = myComponentName.getName();
        if (completeConstructor) {
            DartClass dartClass = PsiTreeUtil.getParentOfType(myComponentName, DartClass.class);
            assert dartClass != null;
            return dartClass.getName() + "." + result;
        }
        return StringUtil.notNullize(result);
    }

    private boolean isCompleteConstructorComponent() {
        boolean isConstructor = myComponentName.getParent() instanceof DartNamedConstructorDeclaration
            || myComponentName.getParent() instanceof DartFactoryConstructorDeclaration;
        return isConstructor && myConstructorCompletion;
    }

    @Override
    public void renderElement(LookupElementPresentation presentation) {
        ItemPresentation myComponentNamePresentation = myComponentName.getPresentation();
        if (myComponentNamePresentation == null) {
            presentation.setItemText(getLookupString());
            return;
        }
        LocalizeValue text = myComponentNamePresentation.getPresentableText();
        presentation.setItemText(text.get());
        presentation.setIcon(myComponentNamePresentation.getIcon());
        String pkg = myComponentNamePresentation.getLocationString();
        if (StringUtil.isNotEmpty(pkg)) {
            presentation.setTailText(" " + pkg, true);
        }
    }

    @Override
    public void handleInsert(InsertionContext context) {
        PsiElement parent = myComponentName.getParent();
        DartComponentType componentType = DartComponentType.typeOf(parent);
        boolean isGetterSetter = parent instanceof DartGetterDeclaration || parent instanceof DartSetterDeclaration;
        if (!isGetterSetter &&
            parent instanceof DartComponent
            &&
            (componentType == DartComponentType.FUNCTION ||
                componentType == DartComponentType.METHOD ||
                componentType == DartComponentType.CONSTRUCTOR)) {
            String parameterList = DartPresentableUtil.getPresentableParameterList((DartComponent) parent);
            ParenthesesInsertHandler<LookupElement> insertHandler =
                parameterList.isEmpty() ? ParenthesesInsertHandler.NO_PARAMETERS : ParenthesesInsertHandler.WITH_PARAMETERS;
            insertHandler.handleInsert(context, this);
        }
        if (myConstructorCompletion && parent instanceof DartClass dartClass) {
            String name = dartClass.getName();
            DartComponent dartComponent = name != null ? dartClass.findNamedConstructor(name) : null;
            String parameterList = DartPresentableUtil.getPresentableParameterList(dartComponent);
            ParenthesesInsertHandler<LookupElement> insertHandler = dartComponent == null || parameterList.isEmpty()
                ? ParenthesesInsertHandler.NO_PARAMETERS
                : ParenthesesInsertHandler.WITH_PARAMETERS;
            insertHandler.handleInsert(context, this);
        }
    }

    @Nonnull
    @Override
    public Object getObject() {
        return myComponentName;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        return this == o
            || o instanceof DartLookupElement that && myComponentName.equals(that.myComponentName);
    }

    @Override
    public int hashCode() {
        return myComponentName.hashCode();
    }
}
