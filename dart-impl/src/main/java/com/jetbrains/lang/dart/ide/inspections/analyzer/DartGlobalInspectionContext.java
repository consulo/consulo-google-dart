package com.jetbrains.lang.dart.ide.inspections.analyzer;

import com.google.dart.engine.context.AnalysisContext;
import com.google.dart.engine.error.AnalysisError;
import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.analyzer.DartFileBasedSource;
import com.jetbrains.lang.dart.analyzer.DartInProcessAnnotator;
import consulo.application.ApplicationManager;
import consulo.application.progress.ProgressIndicator;
import consulo.application.progress.ProgressManager;
import consulo.language.editor.inspection.GlobalInspectionContext;
import consulo.language.editor.inspection.GlobalInspectionContextExtension;
import consulo.language.editor.inspection.scheme.InspectionToolWrapper;
import consulo.language.editor.inspection.scheme.Tools;
import consulo.language.editor.scope.AnalysisScope;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.search.FileTypeIndex;
import consulo.project.Project;
import consulo.util.dataholder.Key;
import consulo.util.lang.Pair;
import consulo.virtualFileSystem.VirtualFile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class DartGlobalInspectionContext implements GlobalInspectionContextExtension<DartGlobalInspectionContext> {
  static final Key<DartGlobalInspectionContext> KEY = Key.create("DartGlobalInspectionContext");

  private final Map<VirtualFile, AnalysisError[]> libraryRoot2Errors = new HashMap<VirtualFile, AnalysisError[]>();

  public Map<VirtualFile, AnalysisError[]> getLibraryRoot2Errors() {
    return libraryRoot2Errors;
  }

  @Nonnull
  @Override
  public Key<DartGlobalInspectionContext> getID() {
    return KEY;
  }

  @Override
  public void performPreRunActivities(@Nonnull List<Tools> globalTools,
                                      @Nonnull List<Tools> localTools,
                                      @Nonnull GlobalInspectionContext context) {
    final AnalysisScope analysisScope = context.getRefManager().getScope();
    if (analysisScope == null) {
      return;
    }

    final GlobalSearchScope scope = GlobalSearchScope.EMPTY_SCOPE.union(analysisScope.toSearchScope());
    setIndicatorText("Looking for Dart files...");
    final Collection<VirtualFile> dartFiles = FileTypeIndex.getFiles(DartFileType.INSTANCE, scope);

    for (VirtualFile dartFile : dartFiles) {
      analyzeFile(dartFile, context.getProject());
    }
  }

  private void analyzeFile(@Nonnull final VirtualFile virtualFile, @Nonnull final Project project) {
    final DartInProcessAnnotator annotator = new DartInProcessAnnotator();

    final Pair<DartFileBasedSource, AnalysisContext> sourceAndContext = ApplicationManager.getApplication().runReadAction(new
                                                                                                                            Supplier<Pair<DartFileBasedSource, AnalysisContext>>() {
                                                                                                                              @Nullable
                                                                                                                              public Pair<DartFileBasedSource, AnalysisContext> get() {
                                                                                                                                final PsiFile
                                                                                                                                  psiFile =
                                                                                                                                  PsiManager
                                                                                                                                    .getInstance(
                                                                                                                                      project)
                                                                                                                                    .findFile(
                                                                                                                                      virtualFile);
                                                                                                                                if (psiFile == null) {
                                                                                                                                  return null;
                                                                                                                                }
                                                                                                                                return annotator
                                                                                                                                  .collectInformation(
                                                                                                                                    psiFile);
                                                                                                                              }
                                                                                                                            });

    if (sourceAndContext == null) {
      return;
    }

    setIndicatorText("Analyzing " + virtualFile.getName() + "...");

    final AnalysisContext analysisContext = annotator.doAnnotate(sourceAndContext);
    if (analysisContext == null) {
      return;
    }


    libraryRoot2Errors.put(virtualFile, analysisContext.getErrors(DartFileBasedSource.getSource(project, virtualFile)).getErrors());
  }

  private static void setIndicatorText(String text) {
    final ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
    if (indicator != null) {
      indicator.setText(text);
    }
  }

  @Override
  public void performPostRunActivities(@Nonnull List<InspectionToolWrapper> inspections, @Nonnull GlobalInspectionContext context) {
  }

  @Override
  public void cleanup() {
    libraryRoot2Errors.clear();
  }
}
