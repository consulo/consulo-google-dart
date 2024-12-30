package com.jetbrains.lang.dart.ide.runner.server;

import com.jetbrains.lang.dart.ide.runner.base.DartRunConfigurationBase;
import com.jetbrains.lang.dart.ide.runner.server.ui.DartCommandLineConfigurationEditorForm;
import consulo.execution.configuration.RunConfiguration;
import consulo.execution.configuration.RunProfileState;
import consulo.execution.configuration.ui.SettingsEditor;
import consulo.execution.executor.Executor;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.process.ExecutionException;
import consulo.project.Project;
import consulo.util.io.PathUtil;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class DartCommandLineRunConfiguration extends DartRunConfigurationBase {
  private
  @Nonnull
  DartCommandLineRunnerParameters myRunnerParameters = new DartCommandLineRunnerParameters();

  public DartCommandLineRunConfiguration(String name, Project project, DartCommandLineRunConfigurationType configurationType) {
    super(project, configurationType.getConfigurationFactories()[0], name);
  }

  @Override
  @Nonnull
  public DartCommandLineRunnerParameters getRunnerParameters() {
    return myRunnerParameters;
  }

  @Override
  @Nonnull
  public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
    return new DartCommandLineConfigurationEditorForm(getProject());
  }

  @Override
  public RunProfileState getState(@Nonnull Executor executor, @Nonnull ExecutionEnvironment env) throws ExecutionException {
    return new DartCommandLineRunningState(env);
  }

  @Override
  @Nullable
  public String suggestedName() {
    final String filePath = myRunnerParameters.getFilePath();
    return filePath == null ? null : PathUtil.getFileName(filePath);
  }

  @Override
  public DartCommandLineRunConfiguration clone() {
    final DartCommandLineRunConfiguration clone = (DartCommandLineRunConfiguration)super.clone();
    clone.myRunnerParameters = myRunnerParameters.clone();
    return clone;
  }
}
