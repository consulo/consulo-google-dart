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
import consulo.navigation.ItemPresentation;
import consulo.util.lang.StringUtil;

import jakarta.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Fedor.Korotkov
 */
public class DartLookupElement extends LookupElement {
  private final DartComponentName myComponentName;
  private final boolean myConstructorCompletion;

  public static Collection<DartLookupElement> convert(@Nonnull Collection<DartComponentName> componentNames,
                                                      boolean constructorCompletion) {
    final Map<String, DartLookupElement> result = new HashMap<String, DartLookupElement>();
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
    final boolean completeConstructor = isCompleteConstructorComponent();
    final String result = myComponentName.getName();
    if (completeConstructor) {
      final DartClass dartClass = PsiTreeUtil.getParentOfType(myComponentName, DartClass.class);
      assert dartClass != null;
      return dartClass.getName() + "." + result;
    }
    return StringUtil.notNullize(result);
  }

  private boolean isCompleteConstructorComponent() {
    final boolean isConstructor = myComponentName.getParent() instanceof DartNamedConstructorDeclaration ||
      myComponentName.getParent() instanceof DartFactoryConstructorDeclaration;
    return isConstructor && myConstructorCompletion;
  }

  @Override
  public void renderElement(LookupElementPresentation presentation) {
    final ItemPresentation myComponentNamePresentation = myComponentName.getPresentation();
    if (myComponentNamePresentation == null) {
      presentation.setItemText(getLookupString());
      return;
    }
    String text = myComponentNamePresentation.getPresentableText();
    presentation.setItemText(text);
    presentation.setIcon(myComponentNamePresentation.getIcon());
    final String pkg = myComponentNamePresentation.getLocationString();
    if (StringUtil.isNotEmpty(pkg)) {
      presentation.setTailText(" " + pkg, true);
    }
  }

  @Override
  public void handleInsert(InsertionContext context) {
    final PsiElement parent = myComponentName.getParent();
    final DartComponentType componentType = DartComponentType.typeOf(parent);
    final boolean isGetterSetter = parent instanceof DartGetterDeclaration || parent instanceof DartSetterDeclaration;
    if (!isGetterSetter &&
      parent instanceof DartComponent
      &&
      (componentType == DartComponentType.FUNCTION ||
        componentType == DartComponentType.METHOD ||
        componentType == DartComponentType.CONSTRUCTOR)) {
      final String parameterList = DartPresentableUtil.getPresentableParameterList((DartComponent)parent);
      final ParenthesesInsertHandler<LookupElement> insertHandler =
        parameterList.isEmpty() ? ParenthesesInsertHandler.NO_PARAMETERS : ParenthesesInsertHandler.WITH_PARAMETERS;
      insertHandler.handleInsert(context, this);
    }
    if (myConstructorCompletion && parent instanceof DartClass) {
      final String name = ((DartClass)parent).getName();
      final DartComponent dartComponent = name != null ? ((DartClass)parent).findNamedConstructor(name) : null;
      final String parameterList = DartPresentableUtil.getPresentableParameterList(dartComponent);
      final ParenthesesInsertHandler<LookupElement> insertHandler =
        dartComponent == null || parameterList.isEmpty()
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof DartLookupElement)) return false;

    return myComponentName.equals(((DartLookupElement)o).myComponentName);
  }

  @Override
  public int hashCode() {
    return myComponentName.hashCode();
  }
}
