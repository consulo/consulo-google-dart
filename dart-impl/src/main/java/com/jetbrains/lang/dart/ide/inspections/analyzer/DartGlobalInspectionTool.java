package com.jetbrains.lang.dart.ide.inspections.analyzer;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.google.dart.engine.error.AnalysisError;
import com.intellij.analysis.AnalysisScope;
import com.intellij.codeInspection.GlobalInspectionContext;
import com.intellij.codeInspection.GlobalInspectionTool;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptionsProcessor;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

public class DartGlobalInspectionTool extends GlobalInspectionTool
{
	@Override
	public void runInspection(@Nonnull AnalysisScope scope, @Nonnull final InspectionManager manager, @Nonnull final GlobalInspectionContext globalContext, @Nonnull final ProblemDescriptionsProcessor problemDescriptionsProcessor)
	{
		final DartGlobalInspectionContext inspectionContext = globalContext.getExtension(DartGlobalInspectionContext.KEY);
		if(inspectionContext == null)
		{
			return;
		}

		ApplicationManager.getApplication().runReadAction(new Runnable()
		{
			@Override
			public void run()
			{
				for(Map.Entry<VirtualFile, AnalysisError[]> entry : inspectionContext.getLibraryRoot2Errors().entrySet())
				{
					final VirtualFile file = entry.getKey();
					final AnalysisError[] analysisErrors = entry.getValue();
					for(AnalysisError analysisError : analysisErrors)
					{
						processMessage(file, analysisError, globalContext, manager, problemDescriptionsProcessor);
					}
				}
			}
		});
	}

	@Override
	public boolean isGraphNeeded()
	{
		return false;
	}

	protected void processMessage(final VirtualFile file, final AnalysisError analysisError, final GlobalInspectionContext globalContext, final InspectionManager manager, final ProblemDescriptionsProcessor problemDescriptionsProcessor)
	{
		final PsiFile psiFile = PsiManager.getInstance(globalContext.getProject()).findFile(file);
		if(psiFile == null)
		{
			return;
		}

		final ProblemDescriptor descriptor = computeProblemDescriptor(manager, psiFile, analysisError);
		if(descriptor != null)
		{
			problemDescriptionsProcessor.addProblemElement(globalContext.getRefManager().getReference(psiFile), descriptor);
		}
	}

	@Nullable
	private static ProblemDescriptor computeProblemDescriptor(final @Nonnull InspectionManager manager, final @Nonnull PsiFile psiFile, final @Nonnull AnalysisError analysisError)
	{

		final int startOffset = analysisError.getOffset();
		final TextRange textRange = new TextRange(startOffset, startOffset + analysisError.getLength());
		PsiElement element = psiFile.findElementAt(startOffset + analysisError.getLength() / 2);
		while(element != null && textRange.getStartOffset() < element.getTextOffset())
		{
			element = element.getParent();
		}

		if(element != null && textRange.equals(element.getTextRange()))
		{
			return computeProblemDescriptor(manager, analysisError, element);
		}
		return computeProblemDescriptor(manager, analysisError, psiFile, textRange);
	}

	private static ProblemDescriptor computeProblemDescriptor(InspectionManager manager, AnalysisError analysisError, PsiElement element)
	{
		return manager.createProblemDescriptor(element, analysisError.getMessage(), (LocalQuickFix) null, convertHighlightingType(analysisError), true);
	}


	private static ProblemDescriptor computeProblemDescriptor(InspectionManager manager, AnalysisError analysisError, PsiFile psiFile, TextRange range)
	{
		return manager.createProblemDescriptor(psiFile, range, analysisError.getMessage(), convertHighlightingType(analysisError), true);
	}


	private static ProblemHighlightType convertHighlightingType(AnalysisError analysisError)
	{
		switch(analysisError.getErrorCode().getErrorSeverity())
		{
			case NONE:
				return ProblemHighlightType.INFORMATION;
			case INFO:
				return ProblemHighlightType.INFORMATION;
			case WARNING:
				return ProblemHighlightType.GENERIC_ERROR_OR_WARNING;
			case ERROR:
				return ProblemHighlightType.ERROR;
		}
		return ProblemHighlightType.INFORMATION;
	}
}
