package com.jetbrains.lang.dart.ide.runner.unittest.ui;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.ide.runner.server.ui.DartCommandLineConfigurationEditorForm;
import com.jetbrains.lang.dart.ide.runner.unittest.DartUnitRunConfiguration;
import com.jetbrains.lang.dart.ide.runner.unittest.DartUnitRunnerParameters;
import consulo.configurable.ConfigurationException;
import consulo.execution.ExecutionBundle;
import consulo.execution.configuration.ui.SettingsEditor;
import consulo.execution.ui.awt.EnvironmentVariablesComponent;
import consulo.execution.ui.awt.RawCommandLineEditor;
import consulo.fileChooser.FileChooserDescriptorFactory;
import consulo.project.Project;
import consulo.ui.ex.awt.EnumComboBoxModel;
import consulo.ui.ex.awt.ListCellRendererWrapper;
import consulo.ui.ex.awt.TextFieldWithBrowseButton;
import consulo.util.io.FileUtil;
import consulo.util.lang.StringUtil;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import static com.jetbrains.lang.dart.ide.runner.unittest.DartUnitRunnerParameters.Scope;

public class DartUnitConfigurationEditorForm extends SettingsEditor<DartUnitRunConfiguration> {
  private JPanel myMainPanel;
  private JComboBox myScopeCombo;
  private JLabel myTestFileLabel;
  private TextFieldWithBrowseButton myFileField;
  private JLabel myTestNameLabel;
  private JTextField myTestNameField;
  private RawCommandLineEditor myVMOptions;
  private RawCommandLineEditor myArguments;
  private TextFieldWithBrowseButton myWorkingDirectory;
  private EnvironmentVariablesComponent myEnvironmentVariables;

  public DartUnitConfigurationEditorForm(final Project project) {
    DartCommandLineConfigurationEditorForm.initDartFileTextWithBrowse(project, myFileField);

    myWorkingDirectory.addBrowseFolderListener(ExecutionBundle.message("select.working.directory.message"), null, project,
                                               FileChooserDescriptorFactory.createSingleFolderDescriptor());

    myScopeCombo.setModel(new EnumComboBoxModel<Scope>(Scope.class));
    myScopeCombo.setRenderer(new ListCellRendererWrapper<Scope>() {
      @Override
      public void customize(final JList list, final Scope value, final int index, final boolean selected, final boolean hasFocus) {
        setText(StringUtil.capitalize(value.toString().toLowerCase(Locale.US)));
      }
    });

    myScopeCombo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        onScopeChanged();
      }
    });

    myVMOptions.setDialogCaption(DartBundle.message("config.vmoptions.caption"));
    myArguments.setDialogCaption(DartBundle.message("config.progargs.caption"));

    // 'Environment variables' is the widest label, anchored by myTestFileLabel
    myTestFileLabel.setPreferredSize(myEnvironmentVariables.getLabel().getPreferredSize());
    myEnvironmentVariables.setAnchor(myTestFileLabel);
  }

  @Override
  protected void resetEditorFrom(DartUnitRunConfiguration configuration) {
    final DartUnitRunnerParameters parameters = configuration.getRunnerParameters();

    myScopeCombo.setSelectedItem(parameters.getScope());
    myFileField.setText(FileUtil.toSystemDependentName(StringUtil.notNullize(parameters.getFilePath())));
    myTestNameField.setText(parameters.getScope() == Scope.ALL ? "" : StringUtil.notNullize(parameters.getTestName()));
    myArguments.setText(StringUtil.notNullize(parameters.getArguments()));
    myVMOptions.setText(StringUtil.notNullize(parameters.getVMOptions()));
    myWorkingDirectory.setText(FileUtil.toSystemDependentName(StringUtil.notNullize(parameters.getWorkingDirectory())));
    myEnvironmentVariables.setEnvs(parameters.getEnvs());
    myEnvironmentVariables.setPassParentEnvs(parameters.isIncludeParentEnvs());

    onScopeChanged();
  }

  @Override
  protected void applyEditorTo(DartUnitRunConfiguration configuration) throws ConfigurationException {
    final DartUnitRunnerParameters parameters = configuration.getRunnerParameters();

    final Scope scope = (Scope)myScopeCombo.getSelectedItem();
    parameters.setScope(scope);
    parameters.setFilePath(StringUtil.nullize(FileUtil.toSystemIndependentName(myFileField.getText()
                                                                                                                                .trim()),
                                              true));
    parameters.setTestName(scope == Scope.ALL ? null : StringUtil.nullize(myTestNameField.getText()));
    parameters.setArguments(StringUtil.nullize(myArguments.getText(), true));
    parameters.setVMOptions(StringUtil.nullize(myVMOptions.getText(), true));
    parameters.setWorkingDirectory(StringUtil.nullize(FileUtil.toSystemIndependentName(myWorkingDirectory.getText().trim()), true));
    parameters.setEnvs(myEnvironmentVariables.getEnvs());
    parameters.setIncludeParentEnvs(myEnvironmentVariables.isPassParentEnvs());
  }

  private void onScopeChanged() {
    final Scope scope = (Scope)myScopeCombo.getSelectedItem();
    myTestNameLabel.setVisible(scope == Scope.GROUP || scope == Scope.METHOD);
    myTestNameField.setVisible(scope == Scope.GROUP || scope == Scope.METHOD);
    myTestNameLabel.setText(scope == Scope.GROUP ? DartBundle.message("dart.unit.group.name") : DartBundle.message("dart.unit.method.name"));
  }

  @Nonnull
  @Override
  protected JComponent createEditor() {
    return myMainPanel;
  }
}
