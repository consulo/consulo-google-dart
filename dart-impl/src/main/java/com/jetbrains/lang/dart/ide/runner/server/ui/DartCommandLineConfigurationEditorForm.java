package com.jetbrains.lang.dart.ide.runner.server.ui;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.ide.DartWritingAccessProvider;
import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineRunConfiguration;
import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineRunnerParameters;
import consulo.configurable.ConfigurationException;
import consulo.execution.ExecutionBundle;
import consulo.execution.configuration.ui.SettingsEditor;
import consulo.execution.ui.awt.EnvironmentVariablesComponent;
import consulo.execution.ui.awt.RawCommandLineEditor;
import consulo.fileChooser.FileChooserDescriptorFactory;
import consulo.language.editor.ui.TreeFileChooser;
import consulo.language.editor.ui.TreeFileChooserFactory;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.project.Project;
import consulo.ui.ex.awt.TextFieldWithBrowseButton;
import consulo.util.io.FileUtil;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;

import jakarta.annotation.Nonnull;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DartCommandLineConfigurationEditorForm extends SettingsEditor<DartCommandLineRunConfiguration> {
  private JPanel myMainPanel;
  private JLabel myDartFileLabel;
  private TextFieldWithBrowseButton myFileField;
  private RawCommandLineEditor myVMOptions;
  private RawCommandLineEditor myArguments;
  private TextFieldWithBrowseButton myWorkingDirectory;
  private EnvironmentVariablesComponent myEnvironmentVariables;

  public DartCommandLineConfigurationEditorForm(final Project project) {
    initDartFileTextWithBrowse(project, myFileField);

    myWorkingDirectory.addBrowseFolderListener(ExecutionBundle.message("select.working.directory.message"), null, project,
                                               FileChooserDescriptorFactory.createSingleFolderDescriptor());

    myVMOptions.setDialogCaption(DartBundle.message("config.vmoptions.caption"));
    myArguments.setDialogCaption(DartBundle.message("config.progargs.caption"));

    // 'Environment variables' is the widest label, anchored by myDartFileLabel
    myDartFileLabel.setPreferredSize(myEnvironmentVariables.getLabel().getPreferredSize());
    myEnvironmentVariables.setAnchor(myDartFileLabel);
  }

  public static void initDartFileTextWithBrowse(final @Nonnull Project project, final @Nonnull TextFieldWithBrowseButton textWithBrowse) {
    textWithBrowse.getButton().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        final String initialPath = FileUtil.toSystemIndependentName(textWithBrowse.getText().trim());
        final VirtualFile initialFile = initialPath.isEmpty() ? null : LocalFileSystem.getInstance().findFileByPath(initialPath);
        final PsiFile initialPsiFile = initialFile == null ? null : PsiManager.getInstance(project).findFile(initialFile);

        TreeFileChooser fileChooser = TreeFileChooserFactory.getInstance(project).createFileChooser(DartBundle.message("choose.dart.main" +
                                                                                                                         ".file"),
                                                                                                    initialPsiFile,
                                                                                                    DartFileType.INSTANCE,
                                                                                                    file -> !DartWritingAccessProvider.isInDartSdkOrDartPackagesFolder(
                                                                                                      file));

        fileChooser.showDialog();

        final PsiFile selectedFile = fileChooser.getSelectedFile();
        final VirtualFile virtualFile = selectedFile == null ? null : selectedFile.getVirtualFile();
        if (virtualFile != null) {
          final String path = FileUtil.toSystemDependentName(virtualFile.getPath());
          textWithBrowse.setText(path);
        }
      }
    });
  }

  @Override
  protected void resetEditorFrom(final DartCommandLineRunConfiguration configuration) {
    final DartCommandLineRunnerParameters parameters = configuration.getRunnerParameters();

    myFileField.setText(FileUtil.toSystemDependentName(StringUtil.notNullize(parameters.getFilePath())));
    myArguments.setText(StringUtil.notNullize(parameters.getArguments()));
    myVMOptions.setText(StringUtil.notNullize(parameters.getVMOptions()));
    myWorkingDirectory.setText(FileUtil.toSystemDependentName(StringUtil.notNullize(parameters.getWorkingDirectory())));
    myEnvironmentVariables.setEnvs(parameters.getEnvs());
    myEnvironmentVariables.setPassParentEnvs(parameters.isIncludeParentEnvs());
  }

  @Override
  protected void applyEditorTo(final DartCommandLineRunConfiguration configuration) throws ConfigurationException {
    final DartCommandLineRunnerParameters parameters = configuration.getRunnerParameters();

    parameters.setFilePath(StringUtil.nullize(FileUtil.toSystemIndependentName(myFileField.getText().trim()), true));
    parameters.setArguments(StringUtil.nullize(myArguments.getText(), true));
    parameters.setVMOptions(StringUtil.nullize(myVMOptions.getText(), true));
    parameters.setWorkingDirectory(StringUtil.nullize(FileUtil.toSystemIndependentName(myWorkingDirectory.getText().trim()), true));
    parameters.setEnvs(myEnvironmentVariables.getEnvs());
    parameters.setIncludeParentEnvs(myEnvironmentVariables.isPassParentEnvs());
  }

  @Nonnull
  @Override
  protected JComponent createEditor() {
    return myMainPanel;
  }
}
