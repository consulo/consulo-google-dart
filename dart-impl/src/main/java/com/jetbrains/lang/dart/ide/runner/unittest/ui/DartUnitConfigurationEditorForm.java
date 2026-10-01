package com.jetbrains.lang.dart.ide.runner.unittest.ui;

import com.jetbrains.lang.dart.ide.runner.server.ui.DartCommandLineParametersPanel;
import com.jetbrains.lang.dart.ide.runner.unittest.DartUnitRunConfiguration;
import com.jetbrains.lang.dart.ide.runner.unittest.DartUnitRunnerParameters;
import consulo.configurable.ConfigurationException;
import consulo.execution.configuration.ui.SettingsEditor;
import consulo.google.dart.localize.DartLocalize;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.ui.ComboBox;
import consulo.ui.Component;
import consulo.ui.Label;
import consulo.ui.TextBox;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.util.FormBuilder;
import consulo.util.lang.StringUtil;
import jakarta.annotation.Nullable;

import static com.jetbrains.lang.dart.ide.runner.unittest.DartUnitRunnerParameters.Scope;

public class DartUnitConfigurationEditorForm extends SettingsEditor<DartUnitRunConfiguration> {
  private final Project myProject;
  private @Nullable Panel myPanel;

  public DartUnitConfigurationEditorForm(final Project project) {
    myProject = project;
  }

  @RequiredUIAccess
  @Override
  protected Component createUIComponent() {
    Panel panel = new Panel();
    myPanel = panel;
    Component component = panel.build();
    panel.onScopeChanged();
    return component;
  }

  @RequiredUIAccess
  @Override
  protected void resetEditorFrom(DartUnitRunConfiguration configuration) {
    Panel panel = myPanel;
    if (panel != null) {
      panel.reset(configuration.getRunnerParameters());
    }
  }

  @RequiredUIAccess
  @Override
  protected void applyEditorTo(DartUnitRunConfiguration configuration) throws ConfigurationException {
    Panel panel = myPanel;
    if (panel != null) {
      panel.apply(configuration.getRunnerParameters());
    }
  }

  private static LocalizeValue getScopeName(final Scope scope) {
    return switch (scope) {
      case METHOD -> DartLocalize.dartUnitScopeMethod();
      case GROUP -> DartLocalize.dartUnitScopeGroup();
      case ALL -> DartLocalize.dartUnitScopeAll();
    };
  }

  private class Panel extends DartCommandLineParametersPanel {
    private final ComboBox<Scope> myScopeCombo;
    private final Label myTestNameLabel;
    private final TextBox myTestNameField;

    @RequiredUIAccess
    private Panel() {
      super(DartUnitConfigurationEditorForm.this.myProject, DartLocalize.runConfigurationTestFileLabel());

      myScopeCombo = ComboBox.create(Scope.values());
      myScopeCombo.setTextRenderer(scope -> scope == null ? LocalizeValue.empty() : getScopeName(scope));

      myTestNameLabel = Label.create(DartLocalize.dartUnitMethodName());
      myTestNameField = TextBox.create();

      myScopeCombo.addValueListener(event -> onScopeChanged());
    }

    @RequiredUIAccess
    @Override
    protected void addBefore(final FormBuilder builder) {
      builder.addLabeled(DartLocalize.dartUnitScope(), myScopeCombo);
    }

    @RequiredUIAccess
    @Override
    protected void addAfterFile(final FormBuilder builder) {
      builder.addLabeled(myTestNameLabel, myTestNameField);
    }

    @RequiredUIAccess
    private void onScopeChanged() {
      final Scope scope = myScopeCombo.getValue();
      final boolean visible = scope == Scope.GROUP || scope == Scope.METHOD;
      myTestNameLabel.setVisible(visible);
      myTestNameField.setVisible(visible);
      myTestNameLabel.setText(scope == Scope.GROUP ? DartLocalize.dartUnitGroupName() : DartLocalize.dartUnitMethodName());
    }

    @RequiredUIAccess
    private void reset(final DartUnitRunnerParameters parameters) {
      myScopeCombo.setValue(parameters.getScope());
      myTestNameField.setValue(parameters.getScope() == Scope.ALL ? "" : StringUtil.notNullize(parameters.getTestName()));

      resetEditorFrom(parameters);

      onScopeChanged();
    }

    @RequiredUIAccess
    private void apply(final DartUnitRunnerParameters parameters) {
      final Scope scope = myScopeCombo.getValue();
      parameters.setScope(scope);
      parameters.setTestName(scope == Scope.ALL ? null : StringUtil.nullize(myTestNameField.getValue()));

      applyEditorTo(parameters);
    }
  }
}
