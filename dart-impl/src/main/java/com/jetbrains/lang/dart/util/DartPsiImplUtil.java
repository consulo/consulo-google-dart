package com.jetbrains.lang.dart.util;

import com.jetbrains.lang.dart.ide.index.DartLibraryIndex;
import com.jetbrains.lang.dart.psi.*;
import com.jetbrains.lang.dart.resolve.DartResolveProcessor;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.ResolveState;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.util.io.FileUtil;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DartPsiImplUtil {

  @Nonnull
  public static String getPath(@Nonnull DartPartStatement partStatement) {
    final DartExpression expression = partStatement.getPathOrLibraryReference();
    return FileUtil.toSystemIndependentName(StringUtil.unquoteString(expression.getText()));
  }

  @Nonnull
  public static String getLibraryName(@Nonnull DartLibraryStatement libraryStatement) {
    final DartQualifiedComponentName componentName = libraryStatement.getQualifiedComponentName();
    return StringUtil.notNullize(componentName.getName());
  }

  @Nonnull
  public static String getUri(@Nonnull DartImportOrExportStatement importStatement) {
    final DartExpression expression = importStatement.getLibraryExpression();
    return StringUtil.unquoteString(expression.getText());
  }

  @Nonnull
  public static String getLibraryName(@Nonnull DartPartOfStatement importStatement) {
    final DartLibraryId expression = importStatement.getLibraryId();
    return FileUtil.toSystemIndependentName(StringUtil.unquoteString(expression.getText()));
  }

  @Nullable
  public static PsiElement resolveReference(@Nonnull DartType dartType) {
    final DartExpression expression = dartType.getReferenceExpression();
    final String typeName = expression.getText();
    if (typeName.indexOf('.') != -1) {
      return ((DartReference)expression).resolve();
    }
    List<DartComponentName> result = new ArrayList<DartComponentName>();
    final DartResolveProcessor dartResolveProcessor = new DartResolveProcessor(result, typeName);

    final VirtualFile virtualFile = DartResolveUtil.getRealVirtualFile(dartType.getContainingFile());
    if (virtualFile != null) {
      DartResolveUtil.processTopLevelDeclarations(dartType, dartResolveProcessor, virtualFile, typeName);
    }
    // find type parameter
    if (result.isEmpty()) {
      PsiTreeUtil.treeWalkUp(dartResolveProcessor, dartType, null, ResolveState.initial());
      for (Iterator<DartComponentName> iterator = result.iterator(); iterator.hasNext(); ) {
        if (!(iterator.next().getParent() instanceof DartTypeParameter)) {
          iterator.remove();
        }
      }
    }
    // global
    if (result.isEmpty()) {
      final List<VirtualFile> libraryFile = DartResolveUtil.findLibrary(dartType.getContainingFile());
      DartResolveUtil.processTopLevelDeclarations(dartType, dartResolveProcessor, libraryFile, typeName);
    }
    // dart:core
    if (result.isEmpty()) {
      final List<VirtualFile> libraryFile = DartLibraryIndex.findLibraryClass(dartType, "dart:core");
      DartResolveUtil.processTopLevelDeclarations(dartType, dartResolveProcessor, libraryFile, typeName);
    }
    return result.isEmpty() ? null : result.iterator().next();
  }

  @Nullable
  public static DartComponentName findComponentName(final @Nonnull DartNormalFormalParameter normalFormalParameter) {
    final DartFunctionSignature functionDeclaration = normalFormalParameter.getFunctionSignature();
    final DartFieldFormalParameter fieldFormalParameter = normalFormalParameter.getFieldFormalParameter();
    final DartSimpleFormalParameter simpleFormalParameter = normalFormalParameter.getSimpleFormalParameter();

    if (functionDeclaration != null) {
      return functionDeclaration.getComponentName();
    }
    if (fieldFormalParameter != null) {
      return null;
    }
    if (simpleFormalParameter != null) {
      return simpleFormalParameter.getComponentName();
    }

    assert false : normalFormalParameter.getText();
    return null;
  }

  public static DartExpression getParameterReferenceExpression(DartNamedArgument argument) {
    return PsiTreeUtil.getChildOfType(argument, DartExpression.class);
  }

  public static DartExpression getExpression(DartNamedArgument argument) {
    final DartExpression[] expressions = PsiTreeUtil.getChildrenOfType(argument, DartExpression.class);
    return expressions != null && expressions.length > 1 ? expressions[expressions.length - 1] : null;
  }
}
