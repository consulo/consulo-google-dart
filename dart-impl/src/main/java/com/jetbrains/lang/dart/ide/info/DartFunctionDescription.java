package com.jetbrains.lang.dart.ide.info;

import com.jetbrains.lang.dart.psi.*;
import com.jetbrains.lang.dart.util.DartClassResolveResult;
import com.jetbrains.lang.dart.util.DartPresentableUtil;
import consulo.document.util.TextRange;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author: Fedor.Korotkov
 */
public class DartFunctionDescription {
  private final String name;
  private final String returnType;
  @Nonnull
  private final DartParameterDescription[] myParameters;
  @Nonnull
  private final DartNamedParameterDescription[] myNamedParameters;

  public DartFunctionDescription(String name,
                                 String type,
                                 @Nonnull DartParameterDescription[] parameters,
                                 @Nonnull DartNamedParameterDescription[] namedParameters) {
    this.name = name;
    returnType = type;
    myParameters = parameters;
    myNamedParameters = namedParameters;
  }

  public String getName() {
    return name;
  }

  public String getReturnType() {
    return returnType;
  }

  @Nonnull
  public DartParameterDescription[] getParameters() {
    return myParameters;
  }

  public String getParametersListPresentableText() {
    final StringBuilder result = new StringBuilder();
    for (DartParameterDescription parameterDescription : myParameters) {
      if (result.length() > 0) {
        result.append(", ");
      }
      result.append(parameterDescription.toString());
    }
    if (myNamedParameters.length > 0) {
      result.append(", [");
    }
    for (int i = 0, length = myNamedParameters.length; i < length; i++) {
      DartNamedParameterDescription namedParameterDescription = myNamedParameters[i];
      if (i > 0) {
        result.append(", ");
      }
      result.append(namedParameterDescription.toString());
    }
    if (myNamedParameters.length > 0) {
      result.append("]");
    }
    return result.toString();
  }

  public TextRange getParameterRange(int index) {
    if (index == -1) {
      return new TextRange(0, 0);
    }
    int startOffset = 0;
    for (int i = 0, length = myParameters.length; i < length; i++) {
      if (i == index) {
        int shift = i == 0 ? 0 : ", ".length();
        return new TextRange(startOffset + shift, startOffset + shift + myParameters[i].toString().length());
      }
      if (i > 0) {
        startOffset += ", ".length();
      }
      startOffset += myParameters[i].toString().length();
    }
    startOffset += ", [".length();
    for (int i = 0, length = myNamedParameters.length; i < length; i++) {
      if ((i + myParameters.length) == index) {
        int shift = i == 0 ? 0 : ", ".length();
        return new TextRange(startOffset + shift, startOffset + shift + myNamedParameters[i].toString().length());
      }
      if (i > 0) {
        startOffset += ", ".length();
      }
      startOffset += myNamedParameters[i].toString().length();
    }
    return new TextRange(0, 0);
  }

  @Nullable
  public static DartFunctionDescription tryGetDescription(DartCallExpression callExpression) {
    final PsiElement target = ((DartReference)callExpression.getExpression()).resolve();
    final PsiElement targetParent = target == null ? null : target.getParent();
    if (target instanceof DartComponentName && targetParent instanceof DartComponent) {
      final DartReference[] references = PsiTreeUtil.getChildrenOfType(callExpression.getExpression(), DartReference.class);
      final DartClassResolveResult resolveResult = (references != null && references.length == 2)
        ? references[0].resolveDartClass()
        : DartClassResolveResult
        .create(PsiTreeUtil.getParentOfType(callExpression, DartClass.class));
      return createDescription((DartComponent)targetParent, resolveResult);
    }
    return null;
  }

  public static DartFunctionDescription createDescription(DartComponent namedComponent, DartClassResolveResult resolveResult) {
    String typeText = "";
    final DartReturnType returnType = PsiTreeUtil.getChildOfType(namedComponent, DartReturnType.class);
    if (returnType != null) {
      typeText = DartPresentableUtil.buildTypeText(namedComponent, returnType.getType(), resolveResult.getSpecialization());
    }
    return new DartFunctionDescription(namedComponent.getName(),
                                       typeText,
                                       DartParameterDescription.getParameters(namedComponent, resolveResult.getSpecialization()),
                                       DartNamedParameterDescription.getParameters(namedComponent, resolveResult.getSpecialization()));
  }
}
