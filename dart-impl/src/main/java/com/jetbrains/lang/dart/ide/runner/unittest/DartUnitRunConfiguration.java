package com.jetbrains.lang.dart.ide.runner.unittest;

import com.jetbrains.lang.dart.ide.runner.base.DartRunConfigurationBase;
import com.jetbrains.lang.dart.ide.runner.unittest.ui.DartUnitConfigurationEditorForm;
import consulo.execution.configuration.ConfigurationFactory;
import consulo.execution.configuration.RunConfiguration;
import consulo.execution.configuration.RunProfileState;
import consulo.execution.configuration.ui.SettingsEditor;
import consulo.execution.executor.Executor;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.google.dart.localize.DartLocalize;
import consulo.process.ExecutionException;
import consulo.project.Project;
import consulo.util.io.PathUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

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
                    return DartLocalize.test0In1(myRunnerParameters.getTestName(), fileName).get();
                case GROUP:
                    return DartLocalize.testGroup0In1(myRunnerParameters.getTestName(), fileName).get();
                case ALL:
                    return DartLocalize.allTestsIn0(fileName).get();
            }
        }
        return null;
    }

    @Override
    public RunConfiguration clone() {
        final DartUnitRunConfiguration clone = (DartUnitRunConfiguration) super.clone();
        clone.myRunnerParameters = myRunnerParameters.clone();
        return clone;
    }
}
