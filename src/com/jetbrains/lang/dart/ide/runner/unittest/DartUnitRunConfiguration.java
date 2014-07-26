package com.jetbrains.lang.dart.ide.runner.unittest;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.util.PathUtil;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.ide.runner.base.DartRunConfigurationBase;
import com.jetbrains.lang.dart.ide.runner.unittest.ui.DartUnitConfigurationEditorForm;

public class DartUnitRunConfiguration extends DartRunConfigurationBase
{

	private
	@NotNull
	DartUnitRunnerParameters myRunnerParameters = new DartUnitRunnerParameters();

	protected DartUnitRunConfiguration(final Project project, final ConfigurationFactory factory, final String name)
	{
		super(project, factory, name);
	}

	@Override
	@NotNull
	public DartUnitRunnerParameters getRunnerParameters()
	{
		return myRunnerParameters;
	}

	@NotNull
	@Override
	public SettingsEditor<? extends RunConfiguration> getConfigurationEditor()
	{
		return new DartUnitConfigurationEditorForm(getProject());
	}

	@Override
	@Nullable
	public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment env) throws ExecutionException
	{
		return new DartUnitRunningState(env);
	}

	@Override
	public String suggestedName()
	{
		final String path = myRunnerParameters.getFilePath();
		if(path != null)
		{
			final String fileName = PathUtil.getFileName(path);
			switch(myRunnerParameters.getScope())
			{
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
	public RunConfiguration clone()
	{
		final DartUnitRunConfiguration clone = (DartUnitRunConfiguration) super.clone();
		clone.myRunnerParameters = myRunnerParameters.clone();
		return clone;
	}
}
