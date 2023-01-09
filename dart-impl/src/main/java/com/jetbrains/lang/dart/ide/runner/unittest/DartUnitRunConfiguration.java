package com.jetbrains.lang.dart.ide.runner.unittest;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.ide.runner.base.DartRunConfigurationBase;
import com.jetbrains.lang.dart.ide.runner.unittest.ui.DartUnitConfigurationEditorForm;
import consulo.execution.configuration.ConfigurationFactory;
import consulo.execution.configuration.RunConfiguration;
import consulo.execution.configuration.RunProfileState;
import consulo.execution.configuration.ui.SettingsEditor;
import consulo.execution.executor.Executor;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.process.ExecutionException;
import consulo.project.Project;
import consulo.util.io.PathUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DartUnitRunConfiguration extends DartRunConfigurationBase {

  private
  @Nonnull
  DartUnitRunnerParameters myRunnerParameters = new DartUnitRunnerParameters();

  protected DartUnitRunConfiguration(final Project project, final ConfigurationFactory factory, final String name) {
    super(project, factory, name);
  }

  @Override
  @Nonnull
  public DartUnitRunnerParameters getRunnerParameters() {
    return myRunnerParameters;
  }

  @Nonnull
  @Override
  public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
    return new DartUnitConfigurationEditorForm(getProject());
  }

  @Override
  @Nullable
  public RunProfileState getState(@Nonnull Executor executor, @Nonnull ExecutionEnvironment env) throws ExecutionException {
    return new DartUnitRunningState(env);
  }

  @Override
  public String suggestedName() {
    final String path = myRunnerParameters.getFilePath();
    if (path != null) {
      final String fileName = PathUtil.getFileName(path);
      switch (myRunnerParameters.getScope()) {
        case METHOD:
          return DartBundle.message("test.0.in.1", myRunnerParameters.getTestName(), fileName);
        case GROUP:
          return DartBundle.message("test.group.0.in.1", myRunnerParameters.getTestName(), fileName);
        case ALL:
          return DartBundle.message("all.tests.in.0", fileName);
      }
    }
    return null;
  }

  @Override
  public RunConfiguration clone() {
    final DartUnitRunConfiguration clone = (DartUnitRunConfiguration)super.clone();
    clone.myRunnerParameters = myRunnerParameters.clone();
    return clone;
  }
}
