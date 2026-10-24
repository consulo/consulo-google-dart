package com.jetbrains.lang.dart.ide.runner.server;

import com.jetbrains.lang.dart.ide.runner.base.DartFileFragment;
import com.jetbrains.lang.dart.ide.runner.base.DartRunConfigurationBase;
import com.jetbrains.lang.dart.ide.runner.base.DartVmOptionsFragment;
import consulo.execution.configuration.LegacyRunConfigurationConverter;
import consulo.execution.configuration.RunProfileState;
import consulo.execution.configuration.fragment.RunConfigurationFragment;
import consulo.execution.executor.Executor;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.google.dart.localize.DartLocalize;
import consulo.process.ExecutionException;
import consulo.project.Project;
import consulo.util.io.PathUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public class DartCommandLineRunConfiguration extends DartRunConfigurationBase {
  public DartCommandLineRunConfiguration(String name, Project project, DartCommandLineRunConfigurationType configurationType) {
    super(project, configurationType.getConfigurationFactories()[0], name);
  }

  @Override
  protected Class<DartCommandLineRunConfigurationOptions> getOptionsClass() {
    return DartCommandLineRunConfigurationOptions.class;
  }

  @Override
  protected void convertLegacyState(LegacyRunConfigurationConverter converter) {
    super.convertLegacyState(converter);

    DartCommandLineRunConfigurationLegacyState old = converter.readXml(DartCommandLineRunConfigurationLegacyState.class);
    convertLegacyCommandLineState(converter, old, new DartCommandLineRunConfigurationOptions());
  }

  @Override
  public List<RunConfigurationFragment<?>> createFragments() {
    return List.of(
      new DartFileFragment(DartLocalize.runConfigurationFragmentDartFile(), DartLocalize.runConfigurationDartFileLabel()),
      new DartVmOptionsFragment()
    );
  }

  @Override
  @Nonnull
  public DartCommandLineRunnerParameters getRunnerParameters() {
    DartCommandLineRunnerParameters parameters = new DartCommandLineRunnerParameters();
    fillRunnerParameters(parameters);
    return parameters;
  }

  @Override
  public RunProfileState getState(@Nonnull Executor executor, @Nonnull ExecutionEnvironment env) throws ExecutionException {
    return new DartCommandLineRunningState(env);
  }

  @Override
  @Nullable
  public String suggestedName() {
    final String filePath = getFilePath();
    return filePath == null ? null : PathUtil.getFileName(filePath);
  }
}
