package com.jetbrains.lang.dart.ide.inspections.analyzer;

import com.google.dart.engine.error.AnalysisError;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.ApplicationManager;
import consulo.document.util.TextRange;
import consulo.language.Language;
import consulo.language.editor.inspection.*;
import consulo.language.editor.inspection.scheme.InspectionManager;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.editor.scope.AnalysisScope;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.virtualFileSystem.VirtualFile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

@ExtensionImpl
public class DartGlobalInspectionTool extends GlobalInspectionTool {
  @Nullable
  @Override
  public Language getLanguage() {
    return DartLanguage.INSTANCE;
  }

  @Nonnull
  @Override
  public String getGroupDisplayName() {
    return "General";
  }

  @Nonnull
  @Override
  public String getDisplayName() {
    return DartBundle.message("dart.analyzer.inspection.display.name");
  }

  @Nonnull
  @Override
  public HighlightDisplayLevel getDefaultLevel() {
    return HighlightDisplayLevel.WARNING;
  }

  @Override
  public boolean isEnabledByDefault() {
    return true;
  }

  @Override
  public void runInspection(@Nonnull AnalysisScope scope,
                            @Nonnull final InspectionManager manager,
                            @Nonnull final GlobalInspectionContext globalContext,
                            @Nonnull final ProblemDescriptionsProcessor problemDescriptionsProcessor) {
    final DartGlobalInspectionContext inspectionContext = globalContext.getExtension(DartGlobalInspectionContext.KEY);
    if (inspectionContext == null) {
      return;
    }

    ApplicationManager.getApplication().runReadAction(new Runnable() {
      @Override
      public void run() {
        for (Map.Entry<VirtualFile, AnalysisError[]> entry : inspectionContext.getLibraryRoot2Errors().entrySet()) {
          final VirtualFile file = entry.getKey();
          final AnalysisError[] analysisErrors = entry.getValue();
          for (AnalysisError analysisError : analysisErrors) {
            processMessage(file, analysisError, globalContext, manager, problemDescriptionsProcessor);
          }
        }
      }
    });
  }

  @Override
  public boolean isGraphNeeded() {
    return false;
  }

  protected void processMessage(final VirtualFile file,
                                final AnalysisError analysisError,
                                final GlobalInspectionContext globalContext,
                                final InspectionManager manager,
                                final ProblemDescriptionsProcessor problemDescriptionsProcessor) {
    final PsiFile psiFile = PsiManager.getInstance(globalContext.getProject()).findFile(file);
    if (psiFile == null) {
      return;
    }

    final ProblemDescriptor descriptor = computeProblemDescriptor(manager, psiFile, analysisError);
    if (descriptor != null) {
      problemDescriptionsProcessor.addProblemElement(globalContext.getRefManager().getReference(psiFile), descriptor);
    }
  }

  @Nullable
  private static ProblemDescriptor computeProblemDescriptor(final @Nonnull InspectionManager manager,
                                                            final @Nonnull PsiFile psiFile,
                                                            final @Nonnull AnalysisError analysisError) {

    final int startOffset = analysisError.getOffset();
    final TextRange textRange = new TextRange(startOffset, startOffset + analysisError.getLength());
    PsiElement element = psiFile.findElementAt(startOffset + analysisError.getLength() / 2);
    while (element != null && textRange.getStartOffset() < element.getTextOffset()) {
      element = element.getParent();
    }

    if (element != null && textRange.equals(element.getTextRange())) {
      return computeProblemDescriptor(manager, analysisError, element);
    }
    return computeProblemDescriptor(manager, analysisError, psiFile, textRange);
  }

  private static ProblemDescriptor computeProblemDescriptor(InspectionManager manager, AnalysisError analysisError, PsiElement element) {
    return manager.createProblemDescriptor(element,
                                           analysisError.getMessage(),
                                           (LocalQuickFix)null,
                                           convertHighlightingType(analysisError),
                                           true);
  }


  private static ProblemDescriptor computeProblemDescriptor(InspectionManager manager,
                                                            AnalysisError analysisError,
                                                            PsiFile psiFile,
                                                            TextRange range) {
    return manager.createProblemDescriptor(psiFile, range, analysisError.getMessage(), convertHighlightingType(analysisError), true);
  }


  private static ProblemHighlightType convertHighlightingType(AnalysisError analysisError) {
    switch (analysisError.getErrorCode().getErrorSeverity()) {
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
