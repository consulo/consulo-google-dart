package com.jetbrains.lang.dart.ide.runner.server;

import com.jetbrains.lang.dart.ide.DartWritingAccessProvider;
import com.jetbrains.lang.dart.psi.DartFile;
import com.jetbrains.lang.dart.psi.DartImportStatement;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.execution.action.ConfigurationContext;
import consulo.execution.action.RunConfigurationProducer;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.module.content.ProjectRootManager;
import consulo.util.lang.ref.Ref;
import consulo.virtualFileSystem.VirtualFile;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

@ExtensionImpl
public class DartCommandLineRuntimeConfigurationProducer extends RunConfigurationProducer<DartCommandLineRunConfiguration> {
  public DartCommandLineRuntimeConfigurationProducer() {
    super(DartCommandLineRunConfigurationType.getInstance());
  }

  @Override
  protected boolean setupConfigurationFromContext(final @Nonnull DartCommandLineRunConfiguration configuration,
                                                  final @Nonnull ConfigurationContext context,
                                                  final @Nonnull Ref<PsiElement> sourceElement) {
    final VirtualFile dartFile = findRunnableDartFile(context);
    if (dartFile != null) {
      configuration.getRunnerParameters().setFilePath(dartFile.getPath());
      configuration.getRunnerParameters().setWorkingDirectory(dartFile.getParent().getPath());
      configuration.setGeneratedName();
      return true;
    }
    return false;
  }

  @Override
  public boolean isConfigurationFromContext(final @Nonnull DartCommandLineRunConfiguration configuration,
                                            final @Nonnull ConfigurationContext context) {
    final VirtualFile dartFile = findRunnableDartFile(context);
    return dartFile != null && dartFile.getPath().equals(configuration.getRunnerParameters().getFilePath());
  }

  @Nullable
  public static VirtualFile findRunnableDartFile(final @Nonnull ConfigurationContext context) {
    final PsiElement psiLocation = context.getPsiLocation();
    final PsiFile psiFile = psiLocation == null ? null : psiLocation.getContainingFile();
    final VirtualFile virtualFile = DartResolveUtil.getRealVirtualFile(psiFile);

    if (psiFile instanceof DartFile &&
      virtualFile != null &&
      ProjectRootManager.getInstance(context.getProject()).getFileIndex().isInContent(virtualFile) &&
      !DartWritingAccessProvider.isInDartSdkOrDartPackagesFolder(psiFile.getProject(), virtualFile) &&
      DartResolveUtil.getMainFunction(psiFile) != null &&
      !hasImport((DartFile)psiFile, "dart:html")) {
      return virtualFile;
    }

    return null;
  }

  private static boolean hasImport(final @Nonnull DartFile psiFile, final @Nonnull String importText) {
    final DartImportStatement[] importStatements = PsiTreeUtil.getChildrenOfType(psiFile, DartImportStatement.class);
    if (importStatements == null) {
      return false;
    }

    for (DartImportStatement importStatement : importStatements) {
      if (importText.equals(importStatement.getUri())) {
        return true;
      }
    }

    return false;
  }
}
