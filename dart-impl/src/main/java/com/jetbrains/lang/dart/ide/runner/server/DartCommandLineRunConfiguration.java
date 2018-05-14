package com.jetbrains.lang.dart.ide.runner.server;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.util.PathUtil;
import com.jetbrains.lang.dart.ide.runner.base.DartRunConfigurationBase;
import com.jetbrains.lang.dart.ide.runner.server.ui.DartCommandLineConfigurationEditorForm;

public class DartCommandLineRunConfiguration extends DartRunConfigurationBase
{
	private
	@Nonnull
	DartCommandLineRunnerParameters myRunnerParameters = new DartCommandLineRunnerParameters();

	public DartCommandLineRunConfiguration(String name, Project project, DartCommandLineRunConfigurationType configurationType)
	{
		super(project, configurationType.getConfigurationFactories()[0], name);
	}

	@Override
	@Nonnull
	public DartCommandLineRunnerParameters getRunnerParameters()
	{
		return myRunnerParameters;
	}

	@Override
	@Nonnull
	public SettingsEditor<? extends RunConfiguration> getConfigurationEditor()
	{
		return new DartCommandLineConfigurationEditorForm(getProject());
	}

	@Override
	public RunProfileState getState(@Nonnull Executor executor, @Nonnull ExecutionEnvironment env) throws ExecutionException
	{
		return new DartCommandLineRunningState(env);
	}

	@Override
	@Nullable
	public String suggestedName()
	{
		final String filePath = myRunnerParameters.getFilePath();
		return filePath == null ? null : PathUtil.getFileName(filePath);
	}

	@Override
	public DartCommandLineRunConfiguration clone()
	{
		final DartCommandLineRunConfiguration clone = (DartCommandLineRunConfiguration) super.clone();
		clone.myRunnerParameters = myRunnerParameters.clone();
		return clone;
	}
}
