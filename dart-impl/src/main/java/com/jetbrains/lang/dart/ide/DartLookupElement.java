package com.jetbrains.lang.dart.ide;

import gnu.trove.THashMap;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nonnull;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponent;
import com.jetbrains.lang.dart.psi.DartComponentName;
import com.jetbrains.lang.dart.psi.DartFactoryConstructorDeclaration;
import com.jetbrains.lang.dart.psi.DartGetterDeclaration;
import com.jetbrains.lang.dart.psi.DartNamedConstructorDeclaration;
import com.jetbrains.lang.dart.psi.DartSetterDeclaration;
import com.jetbrains.lang.dart.util.DartPresentableUtil;
import consulo.awt.TargetAWT;

/**
 * @author: Fedor.Korotkov
 */
public class DartLookupElement extends LookupElement {
  private final DartComponentName myComponentName;
  private final boolean myConstructorCompletion;

  public static Collection<DartLookupElement> convert(@Nonnull Collection<DartComponentName> componentNames,
                                                      boolean constructorCompletion) {
    final Map<String, DartLookupElement> result = new THashMap<String, DartLookupElement>();
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
    presentation.setIcon(TargetAWT.to(myComponentNamePresentation.getIcon()));
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
