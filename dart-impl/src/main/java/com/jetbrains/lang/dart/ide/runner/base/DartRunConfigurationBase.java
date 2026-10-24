package com.jetbrains.lang.dart.ide.runner.base;

import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineRunConfigurationLegacyState;
import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineRunConfigurationOptions;
import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineRunnerParameters;
import consulo.execution.CommonProgramRunConfigurationParameters;
import consulo.execution.RuntimeConfigurationException;
import consulo.execution.configuration.ConfigurationFactory;
import consulo.execution.configuration.LegacyRunConfigurationConverter;
import consulo.execution.configuration.LocatableConfigurationBase;
import consulo.execution.configuration.fragment.EnvironmentFragmentState;
import consulo.execution.configuration.fragment.ProgramParametersFragmentState;
import consulo.execution.configuration.fragment.WorkingDirectoryFragmentState;
import consulo.language.editor.refactoring.event.RefactoringElementListener;
import consulo.language.editor.refactoring.event.RefactoringListenerProvider;
import consulo.language.editor.refactoring.event.UndoRefactoringElementAdapter;
import consulo.language.psi.PsiDirectory;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiFileSystemItem;
import consulo.project.Project;
import consulo.util.io.PathUtil;
import consulo.virtualFileSystem.VirtualFile;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public abstract class DartRunConfigurationBase extends LocatableConfigurationBase implements CommonProgramRunConfigurationParameters,
  RefactoringListenerProvider {

  protected DartRunConfigurationBase(final Project project, final ConfigurationFactory factory, final String name) {
    super(project, factory, name);
  }

  @Override
  public DartCommandLineRunConfigurationOptions getOptions() {
    return (DartCommandLineRunConfigurationOptions)super.getOptions();
  }

  /**
   * A snapshot of the settings for running; changes to it are kept only through {@link #setRunnerParameters}.
   */
  @Nonnull
  public abstract DartCommandLineRunnerParameters getRunnerParameters();

  public void setRunnerParameters(@Nonnull DartCommandLineRunnerParameters parameters) {
    setFilePath(parameters.getFilePath());
    setVmOptions(parameters.getVMOptions());
    setProgramParameters(parameters.getArguments());
    setWorkingDirectory(parameters.getWorkingDirectory());
    setEnvs(parameters.getEnvs());
    setPassParentEnvs(parameters.isIncludeParentEnvs());
  }

  protected void fillRunnerParameters(@Nonnull DartCommandLineRunnerParameters parameters) {
    parameters.setFilePath(getFilePath());
    parameters.setVMOptions(getVmOptions());
    parameters.setArguments(getProgramParameters());
    parameters.setWorkingDirectory(getWorkingDirectory());
    parameters.setEnvs(getEnvs());
    parameters.setIncludeParentEnvs(isPassParentEnvs());
  }

  protected static void convertLegacyCommandLineState(@Nonnull LegacyRunConfigurationConverter converter,
                                                      @Nonnull DartCommandLineRunConfigurationLegacyState old,
                                                      @Nonnull DartCommandLineRunConfigurationOptions options) {
    options.setFilePath(old.getFilePath());
    options.setVmOptions(old.getVMOptions());
    converter.setOptions(options);

    converter.addFragment(new ProgramParametersFragmentState(old.getArguments()));
    converter.addFragment(new WorkingDirectoryFragmentState(old.getWorkingDirectory()));
    converter.addFragment(new EnvironmentFragmentState(old.getEnvs(), old.isIncludeParentEnvs()));
  }

  @Nullable
  public String getFilePath() {
    return getOptions().getFilePath();
  }

  public void setFilePath(@Nullable String filePath) {
    getOptions().setFilePath(filePath);
  }

  @Nullable
  public String getVmOptions() {
    return getOptions().getVmOptions();
  }

  public void setVmOptions(@Nullable String vmOptions) {
    getOptions().setVmOptions(vmOptions);
  }

  @Override
  public void checkConfiguration() throws RuntimeConfigurationException {
    getRunnerParameters().check(getProject());
  }

  @Nullable
  @Override
  public RefactoringElementListener getRefactoringElementListener(final PsiElement element) {
    if (!(element instanceof PsiFileSystemItem)) {
      return null;
    }

    final String filePath = getFilePath();
    final VirtualFile file = filePath == null ? null : ((PsiFileSystemItem)element).getVirtualFile();
    if (file == null) {
      return null;
    }

    final String affectedPath = file.getPath();
    if (element instanceof PsiFile) {
      if (filePath.equals(affectedPath)) {
        return new RenameRefactoringListener(affectedPath);
      }
    }
    if (element instanceof PsiDirectory) {
      if (filePath.startsWith(affectedPath + "/")) {
        return new RenameRefactoringListener(affectedPath);
      }
    }

    return null;
  }

  private class RenameRefactoringListener extends UndoRefactoringElementAdapter {
    private
    @Nonnull
    String myAffectedPath;

    private RenameRefactoringListener(final @Nonnull String affectedPath) {
      myAffectedPath = affectedPath;
    }

    private String getNewPathAndUpdateAffectedPath(final @Nonnull PsiElement newElement) {
      final String oldPath = getFilePath();

      final VirtualFile newFile = newElement instanceof PsiFileSystemItem ? ((PsiFileSystemItem)newElement).getVirtualFile() : null;
      if (newFile != null && oldPath != null && oldPath.startsWith(myAffectedPath)) {
        final String newPath = newFile.getPath() + oldPath.substring(myAffectedPath.length());
        myAffectedPath = newFile.getPath(); // needed if refactoring will be undone
        return newPath;
      }

      return oldPath;
    }

    @Override
    protected void refactored(@Nonnull final PsiElement element, @Nullable final String oldQualifiedName) {
      final boolean generatedName = getName().equals(suggestedName());
      final String filePath = getFilePath();
      final boolean updateWorkingDir = filePath != null && PathUtil.getParentPath(filePath).equals(getWorkingDirectory());

      final String newPath = getNewPathAndUpdateAffectedPath(element);
      setFilePath(newPath);

      if (updateWorkingDir) {
        setWorkingDirectory(PathUtil.getParentPath(newPath));
      }

      if (generatedName) {
        setGeneratedName();
      }
    }
  }
}

