package com.jetbrains.lang.dart.ide.completion;

import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.psi.*;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.completion.*;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.pattern.PsiElementPattern;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.ProcessingContext;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static consulo.language.pattern.PlatformPatterns.psiElement;

@ExtensionImpl
public class DartArgumentNameContributor extends CompletionContributor {
  public DartArgumentNameContributor() {
    final PsiElementPattern.Capture<PsiElement> idInExpression = psiElement().withSuperParent(1, DartId.class).withSuperParent(2,
                                                                                                                               DartReference.class);
    extend(CompletionType.BASIC, idInExpression.withSuperParent(3, DartArgumentList.class), new CompletionProvider() {
      @Override
      public void addCompletions(@Nonnull CompletionParameters parameters, ProcessingContext context, @Nonnull CompletionResultSet result) {
        DartExpression reference = findExpressionFromCallOrNew(parameters);
        PsiElement target = reference instanceof DartReference ? ((DartReference)reference).resolve() : null;
        PsiElement targetComponent = target != null ? target.getParent() : null;
        DartFormalParameterList parameterList = PsiTreeUtil.getChildOfType(targetComponent, DartFormalParameterList.class);
        if (parameterList != null) {
          for (DartNormalFormalParameter parameter : parameterList.getNormalFormalParameterList()) {
            final DartComponentName componentName = parameter.findComponentName();
            if (componentName != null) {
              addParameterName(result, componentName.getName());
            }
          }
          DartNamedFormalParameters namedFormalParameters = parameterList.getNamedFormalParameters();
          List<DartDefaultFormalNamedParameter> namedParameterList = namedFormalParameters != null ? namedFormalParameters
            .getDefaultFormalNamedParameterList() : Collections.<DartDefaultFormalNamedParameter>emptyList();
          for (DartDefaultFormalNamedParameter parameterDescription : namedParameterList) {
            final DartComponentName componentName = parameterDescription.getNormalFormalParameter().findComponentName();
            if (componentName != null) {
              addParameterName(result, componentName.getName());
            }
          }
        }
      }

      private void addParameterName(CompletionResultSet result, @Nullable String parameterName) {
        if (parameterName != null) {
          result.addElement(LookupElementBuilder.create(parameterName));
        }
      }
    });
  }

  @Nullable
  private static DartExpression findExpressionFromCallOrNew(CompletionParameters parameters) {
    DartCallExpression callExpression = PsiTreeUtil.getParentOfType(parameters.getPosition(), DartCallExpression.class);
    if (callExpression != null) {
      return callExpression.getExpression();
    }
    DartNewExpression newExpression = PsiTreeUtil.getParentOfType(parameters.getPosition(), DartNewExpression.class);
    if (newExpression != null) {
      final DartExpression expression = newExpression.getReferenceExpression();
      if (expression != null) {
        return expression;
      }
      final DartType type = newExpression.getType();
      return type != null ? type.getReferenceExpression() : null;
    }
    return null;
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return DartLanguage.INSTANCE;
  }
}
