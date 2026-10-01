package com.jetbrains.lang.dart.ide.runner.server.ui;

import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.ide.DartWritingAccessProvider;
import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineRunnerParameters;
import consulo.execution.localize.ExecutionLocalize;
import consulo.execution.ui.awt.EnvironmentVariablesTextFieldWithBrowseButton;
import consulo.fileChooser.FileChooserDescriptor;
import consulo.fileChooser.FileChooserDescriptorFactory;
import consulo.fileChooser.FileChooserTextBoxBuilder;
import consulo.google.dart.localize.DartLocalize;
import consulo.localize.LocalizeValue;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.process.cmd.ParametersListUtil;
import consulo.project.Project;
import consulo.ui.Component;
import consulo.ui.TextBoxWithExpandAction;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.util.FormBuilder;
import consulo.util.io.FileUtil;
import consulo.util.lang.StringUtil;

public class DartCommandLineParametersPanel {
  private final LocalizeValue myFileLabel;
  private final FileChooserTextBoxBuilder.Controller myFileField;
  private final TextBoxWithExpandAction myVMOptions;
  private final TextBoxWithExpandAction myArguments;
  private final FileChooserTextBoxBuilder.Controller myWorkingDirectory;
  private final EnvironmentVariablesTextFieldWithBrowseButton myEnvironmentVariables;

  @RequiredUIAccess
  public DartCommandLineParametersPanel(final Project project, final LocalizeValue fileLabel) {
    myFileLabel = fileLabel;

    myFileField = FileChooserTextBoxBuilder.create(project)
      .dialogTitle(DartLocalize.chooseDartMainFile())
      .fileChooserDescriptor(new FileChooserDescriptor(true, false, false, false, false, false)
                               .withFileFilter(file -> file.getFileType() == DartFileType.INSTANCE &&
                                                       !DartWritingAccessProvider.isInDartSdkOrDartPackagesFolder(project, file)))
      .build();

    myVMOptions = createParametersField(DartLocalize.configVmoptionsCaption());
    myArguments = createParametersField(DartLocalize.configProgargsCaption());

    myWorkingDirectory = FileChooserTextBoxBuilder.create(project)
      .dialogTitle(ExecutionLocalize.selectWorkingDirectoryMessage())
      .fileChooserDescriptor(FileChooserDescriptorFactory.createSingleFolderDescriptor())
      .build();

    myEnvironmentVariables = new EnvironmentVariablesTextFieldWithBrowseButton();
  }

  @RequiredUIAccess
  private static TextBoxWithExpandAction createParametersField(final LocalizeValue dialogCaption) {
    return TextBoxWithExpandAction.create(
      PlatformIconGroup.actionsShow(),
      dialogCaption.get(),
      ParametersListUtil.DEFAULT_LINE_PARSER,
      ParametersListUtil.DEFAULT_LINE_JOINER
    );
  }

  @RequiredUIAccess
  public Component build() {
    FormBuilder builder = FormBuilder.create();

    addBefore(builder);

    builder.addLabeled(myFileLabel, myFileField.getComponent());

    addAfterFile(builder);

    builder.addLabeled(ExecutionLocalize.runConfigurationJavaVmParametersLabel(), myVMOptions);
    builder.addLabeled(ExecutionLocalize.runConfigurationProgramParameters(), myArguments);
    builder.addLabeled(ExecutionLocalize.runConfigurationWorkingDirectoryLabel(), myWorkingDirectory.getComponent());
    builder.addLabeled(
      LocalizeValue.join(ExecutionLocalize.environmentVariablesComponentTitle(), LocalizeValue.colon()),
      myEnvironmentVariables.getComponent()
    );

    return builder.build();
  }

  @RequiredUIAccess
  protected void addBefore(final FormBuilder builder) {
  }

  @RequiredUIAccess
  protected void addAfterFile(final FormBuilder builder) {
  }

  @RequiredUIAccess
  public void resetEditorFrom(final DartCommandLineRunnerParameters parameters) {
    myFileField.setValue(FileUtil.toSystemDependentName(StringUtil.notNullize(parameters.getFilePath())));
    myArguments.setValue(StringUtil.notNullize(parameters.getArguments()));
    myVMOptions.setValue(StringUtil.notNullize(parameters.getVMOptions()));
    myWorkingDirectory.setValue(FileUtil.toSystemDependentName(StringUtil.notNullize(parameters.getWorkingDirectory())));
    myEnvironmentVariables.setEnvs(parameters.getEnvs());
    myEnvironmentVariables.setPassParentEnvs(parameters.isIncludeParentEnvs());
  }

  @RequiredUIAccess
  public void applyEditorTo(final DartCommandLineRunnerParameters parameters) {
    parameters.setFilePath(StringUtil.nullize(FileUtil.toSystemIndependentName(myFileField.getValue().trim()), true));
    parameters.setArguments(StringUtil.nullize(myArguments.getValue(), true));
    parameters.setVMOptions(StringUtil.nullize(myVMOptions.getValue(), true));
    parameters.setWorkingDirectory(StringUtil.nullize(FileUtil.toSystemIndependentName(myWorkingDirectory.getValue().trim()), true));
    parameters.setEnvs(myEnvironmentVariables.getEnvs());
    parameters.setIncludeParentEnvs(myEnvironmentVariables.isPassParentEnvs());
  }
}
