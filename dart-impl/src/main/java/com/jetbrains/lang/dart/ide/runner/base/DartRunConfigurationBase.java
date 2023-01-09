package com.jetbrains.lang.dart.ide.runner.base;

import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineRunnerParameters;
import consulo.execution.RuntimeConfigurationException;
import consulo.execution.configuration.ConfigurationFactory;
import consulo.execution.configuration.LocatableConfigurationBase;
import consulo.language.editor.refactoring.event.RefactoringElementListener;
import consulo.language.editor.refactoring.event.RefactoringListenerProvider;
import consulo.language.editor.refactoring.event.UndoRefactoringElementAdapter;
import consulo.language.psi.PsiDirectory;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiFileSystemItem;
import consulo.project.Project;
import consulo.util.io.PathUtil;
import consulo.util.xml.serializer.InvalidDataException;
import consulo.util.xml.serializer.SkipDefaultValuesSerializationFilters;
import consulo.util.xml.serializer.WriteExternalException;
import consulo.util.xml.serializer.XmlSerializer;
import consulo.virtualFileSystem.VirtualFile;
import org.jdom.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class DartRunConfigurationBase extends LocatableConfigurationBase implements RefactoringListenerProvider {

  protected DartRunConfigurationBase(final Project project, final ConfigurationFactory factory, final String name) {
    super(project, factory, name);
  }

  @Nonnull
  public abstract DartCommandLineRunnerParameters getRunnerParameters();

  @Override
  public void checkConfiguration() throws RuntimeConfigurationException {
    getRunnerParameters().check(getProject());
  }

  @Override
  public void writeExternal(final Element element) throws WriteExternalException {
    super.writeExternal(element);
    XmlSerializer.serializeInto(getRunnerParameters(), element, new SkipDefaultValuesSerializationFilters());
  }

  @Override
  public void readExternal(final Element element) throws InvalidDataException {
    super.readExternal(element);
    XmlSerializer.deserializeInto(getRunnerParameters(), element);
  }

  @Nullable
  @Override
  public RefactoringElementListener getRefactoringElementListener(final PsiElement element) {
    if (!(element instanceof PsiFileSystemItem)) {
      return null;
    }

    final String filePath = getRunnerParameters().getFilePath();
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
      final String oldPath = getRunnerParameters().getFilePath();

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
      final String filePath = getRunnerParameters().getFilePath();
      final boolean updateWorkingDir = filePath != null && PathUtil.getParentPath(filePath).equals(getRunnerParameters().getWorkingDirectory
        ());

      final String newPath = getNewPathAndUpdateAffectedPath(element);
      getRunnerParameters().setFilePath(newPath);

      if (updateWorkingDir) {
        getRunnerParameters().setWorkingDirectory(PathUtil.getParentPath(newPath));
      }

      if (generatedName) {
        setGeneratedName();
      }
    }
  }
}

