package com.jetbrains.lang.dart.ide.runner.server.ui;

import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineRunConfiguration;
import consulo.configurable.ConfigurationException;
import consulo.execution.configuration.ui.SettingsEditor;
import consulo.google.dart.localize.DartLocalize;
import consulo.project.Project;
import consulo.ui.Component;
import consulo.ui.annotation.RequiredUIAccess;
import jakarta.annotation.Nullable;

public class DartCommandLineConfigurationEditorForm extends SettingsEditor<DartCommandLineRunConfiguration> {
  private final Project myProject;
  private @Nullable DartCommandLineParametersPanel myPanel;

  public DartCommandLineConfigurationEditorForm(final Project project) {
    myProject = project;
  }

  @RequiredUIAccess
  @Override
  protected Component createUIComponent() {
    DartCommandLineParametersPanel panel = new DartCommandLineParametersPanel(myProject, DartLocalize.runConfigurationDartFileLabel());
    myPanel = panel;
    return panel.build();
  }

  @RequiredUIAccess
  @Override
  protected void resetEditorFrom(final DartCommandLineRunConfiguration configuration) {
    DartCommandLineParametersPanel panel = myPanel;
    if (panel != null) {
      panel.resetEditorFrom(configuration.getRunnerParameters());
    }
  }

  @RequiredUIAccess
  @Override
  protected void applyEditorTo(final DartCommandLineRunConfiguration configuration) throws ConfigurationException {
    DartCommandLineParametersPanel panel = myPanel;
    if (panel != null) {
      panel.applyEditorTo(configuration.getRunnerParameters());
    }
  }
}
