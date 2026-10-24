package com.jetbrains.lang.dart.ide.runner.unittest;

import com.jetbrains.lang.dart.ide.runner.base.DartFileFragment;
import com.jetbrains.lang.dart.ide.runner.base.DartRunConfigurationBase;
import com.jetbrains.lang.dart.ide.runner.base.DartVmOptionsFragment;
import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineRunnerParameters;
import consulo.execution.configuration.ConfigurationFactory;
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

public class DartUnitRunConfiguration extends DartRunConfigurationBase {
    protected DartUnitRunConfiguration(final Project project, final ConfigurationFactory factory, final String name) {
        super(project, factory, name);
    }

    @Override
    protected Class<DartUnitRunConfigurationOptions> getOptionsClass() {
        return DartUnitRunConfigurationOptions.class;
    }

    @Override
    public DartUnitRunConfigurationOptions getOptions() {
        return (DartUnitRunConfigurationOptions) super.getOptions();
    }

    @Override
    protected void convertLegacyState(LegacyRunConfigurationConverter converter) {
        super.convertLegacyState(converter);

        DartUnitRunConfigurationLegacyState old = converter.readXml(DartUnitRunConfigurationLegacyState.class);
        DartUnitRunConfigurationOptions options = new DartUnitRunConfigurationOptions();
        DartUnitRunnerParameters.Scope scope = old.getScope();
        options.setScope(scope == null || scope == DartUnitRunnerParameters.Scope.ALL ? null : scope.name());
        options.setTestName(old.getTestName());
        convertLegacyCommandLineState(converter, old, options);
    }

    @Override
    public List<RunConfigurationFragment<?>> createFragments() {
        return List.of(
            new DartUnitScopeFragment(),
            new DartFileFragment(DartLocalize.runConfigurationFragmentTestFile(), DartLocalize.runConfigurationTestFileLabel()),
            new DartVmOptionsFragment()
        );
    }

    @Override
    @Nonnull
    public DartUnitRunnerParameters getRunnerParameters() {
        DartUnitRunnerParameters parameters = new DartUnitRunnerParameters();
        fillRunnerParameters(parameters);
        parameters.setScope(getScope());
        parameters.setTestName(getTestName());
        return parameters;
    }

    @Override
    public void setRunnerParameters(@Nonnull DartCommandLineRunnerParameters parameters) {
        super.setRunnerParameters(parameters);
        if (parameters instanceof DartUnitRunnerParameters unitParameters) {
            setScope(unitParameters.getScope());
            setTestName(unitParameters.getTestName());
        }
    }

    @Nonnull
    public DartUnitRunnerParameters.Scope getScope() {
        String name = getOptions().getScope();
        if (name != null) {
            for (DartUnitRunnerParameters.Scope scope : DartUnitRunnerParameters.Scope.values()) {
                if (scope.name().equals(name)) {
                    return scope;
                }
            }
        }
        return DartUnitRunnerParameters.Scope.ALL;
    }

    public void setScope(@Nonnull DartUnitRunnerParameters.Scope scope) {
        getOptions().setScope(scope == DartUnitRunnerParameters.Scope.ALL ? null : scope.name());
    }

    @Nullable
    public String getTestName() {
        return getOptions().getTestName();
    }

    public void setTestName(@Nullable String testName) {
        getOptions().setTestName(testName);
    }

    @Override
    @Nullable
    public RunProfileState getState(@Nonnull Executor executor, @Nonnull ExecutionEnvironment env) throws ExecutionException {
        return new DartUnitRunningState(env);
    }

    @Override
    public String suggestedName() {
        final String path = getFilePath();
        if (path != null) {
            final String fileName = PathUtil.getFileName(path);
            switch (getScope()) {
                case METHOD:
                    return DartLocalize.test0In1(getTestName(), fileName).get();
                case GROUP:
                    return DartLocalize.testGroup0In1(getTestName(), fileName).get();
                case ALL:
                    return DartLocalize.allTestsIn0(fileName).get();
            }
        }
        return null;
    }
}
