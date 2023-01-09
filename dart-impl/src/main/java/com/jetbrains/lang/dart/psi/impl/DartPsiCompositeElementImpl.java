package com.jetbrains.lang.dart.psi.impl;

import com.jetbrains.lang.dart.psi.*;
import com.jetbrains.lang.dart.util.DartControlFlowUtil;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.impl.psi.ASTWrapperPsiElement;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import consulo.util.collection.ContainerUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class DartPsiCompositeElementImpl extends ASTWrapperPsiElement implements DartPsiCompositeElement {
  public DartPsiCompositeElementImpl(@Nonnull ASTNode node) {
    super(node);
  }

  public IElementType getTokenType() {
    return getNode().getElementType();
  }

  public String toString() {
    return getTokenType().toString();
  }

  @Override
  public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent,
                                     @Nonnull PsiElement place) {
    return processDeclarationsImpl(this, processor, state, lastParent) && super.processDeclarations(processor, state, lastParent, place);
  }

  public static boolean processDeclarationsImpl(@Nullable PsiElement context, PsiScopeProcessor processor, ResolveState state,
                                                @Nullable PsiElement lastParent) {
    if (context == null) {
      return true;
    }
    for (DartComponentName element : getDeclarationElementToProcess(context, lastParent)) {
      if (!processor.execute(element, state)) {
        return false;
      }
    }
    return true;
  }

  private static Set<DartComponentName> getDeclarationElementToProcess(@Nonnull PsiElement context, @Nullable PsiElement lastParent) {
    final PsiElement[] children = context.getChildren();
    final Set<DartComponentName> result =
      DartControlFlowUtil.getSimpleDeclarations(children, lastParent, context instanceof DartStatements);

    for (PsiElement child : children) {
      if (child instanceof DartFormalParameterList) {
        final DartFormalParameterList formalParameterList = (DartFormalParameterList)child;
        final List<DartNormalFormalParameter> normalFormalParameterList = formalParameterList.getNormalFormalParameterList();
        final DartNamedFormalParameters namedFormalParameters = formalParameterList.getNamedFormalParameters();
        if (namedFormalParameters != null) {
          normalFormalParameterList.addAll(ContainerUtil.map(namedFormalParameters.getDefaultFormalNamedParameterList(),
                                                             DartDefaultFormalNamedParameter::getNormalFormalParameter));
        }
        for (DartNormalFormalParameter parameter : normalFormalParameterList) {
          final DartComponentName componentName = parameter.findComponentName();
          if (componentName != null) {
            result.add(componentName);
          }
        }
      }

      if (child instanceof DartTypeParameters) {
        for (DartTypeParameter typeParameter : ((DartTypeParameters)child).getTypeParameterList()) {
          result.add(typeParameter.getComponentName());
        }
      }

      if (child instanceof DartImportStatement) {
        ContainerUtil.addIfNotNull(result, ((DartImportStatement)child).getImportPrefix());
      }

      if (child instanceof DartCatchPart) {
        result.addAll(((DartCatchPart)child).getComponentNameList());
      }
    }

    if (context instanceof DartForStatement) {
      final DartForLoopPartsInBraces loopPartsInBraces = ((DartForStatement)context).getForLoopPartsInBraces();
      final DartForLoopParts loopParts = loopPartsInBraces == null ? null : loopPartsInBraces.getForLoopParts();
      final DartForInPart forInPart = loopParts == null ? null : loopParts.getForInPart();
      final DartComponentName componentName = forInPart == null ? null : forInPart.getComponentName();
      if (componentName != null) {
        result.add(componentName);
      }
      final DartVarAccessDeclaration varDeclaration = forInPart == null ? null : forInPart.getVarAccessDeclaration();
      if (varDeclaration != null) {
        result.add(varDeclaration.getComponentName());
      }
      final DartVarDeclarationList varDeclarationList = loopParts == null ? null : loopParts.getVarDeclarationList();
      if (varDeclarationList != null) {
        DartControlFlowUtil.addFromVarDeclarationList(result, varDeclarationList);
      }
    }

    return result;
  }
}
