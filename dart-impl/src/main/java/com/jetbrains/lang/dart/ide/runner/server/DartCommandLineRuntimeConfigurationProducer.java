package com.jetbrains.lang.dart.ide.runner.server;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.lang.dart.ide.DartWritingAccessProvider;
import com.jetbrains.lang.dart.psi.DartFile;
import com.jetbrains.lang.dart.psi.DartImportStatement;
import com.jetbrains.lang.dart.util.DartResolveUtil;

public class DartCommandLineRuntimeConfigurationProducer extends RunConfigurationProducer<DartCommandLineRunConfiguration>
{
	public DartCommandLineRuntimeConfigurationProducer()
	{
		super(DartCommandLineRunConfigurationType.getInstance());
	}

	@Override
	protected boolean setupConfigurationFromContext(final @Nonnull DartCommandLineRunConfiguration configuration,
			final @Nonnull ConfigurationContext context, final @Nonnull Ref<PsiElement> sourceElement)
	{
		final VirtualFile dartFile = findRunnableDartFile(context);
		if(dartFile != null)
		{
			configuration.getRunnerParameters().setFilePath(dartFile.getPath());
			configuration.getRunnerParameters().setWorkingDirectory(dartFile.getParent().getPath());
			configuration.setGeneratedName();
			return true;
		}
		return false;
	}

	@Override
	public boolean isConfigurationFromContext(final @Nonnull DartCommandLineRunConfiguration configuration,
			final @Nonnull ConfigurationContext context)
	{
		final VirtualFile dartFile = findRunnableDartFile(context);
		return dartFile != null && dartFile.getPath().equals(configuration.getRunnerParameters().getFilePath());
	}

	@Nullable
	public static VirtualFile findRunnableDartFile(final @Nonnull ConfigurationContext context)
	{
		final PsiElement psiLocation = context.getPsiLocation();
		final PsiFile psiFile = psiLocation == null ? null : psiLocation.getContainingFile();
		final VirtualFile virtualFile = DartResolveUtil.getRealVirtualFile(psiFile);

		if(psiFile instanceof DartFile &&
				virtualFile != null &&
				ProjectRootManager.getInstance(context.getProject()).getFileIndex().isInContent(virtualFile) &&
				!DartWritingAccessProvider.isInDartSdkOrDartPackagesFolder(psiFile.getProject(), virtualFile) &&
				DartResolveUtil.getMainFunction(psiFile) != null &&
				!hasImport((DartFile) psiFile, "dart:html"))
		{
			return virtualFile;
		}

		return null;
	}

	private static boolean hasImport(final @Nonnull DartFile psiFile, final @Nonnull String importText)
	{
		final DartImportStatement[] importStatements = PsiTreeUtil.getChildrenOfType(psiFile, DartImportStatement.class);
		if(importStatements == null)
		{
			return false;
		}

		for(DartImportStatement importStatement : importStatements)
		{
			if(importText.equals(importStatement.getUri()))
			{
				return true;
			}
		}

		return false;
	}
}
