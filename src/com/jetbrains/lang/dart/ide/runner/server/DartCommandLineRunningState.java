package com.jetbrains.lang.dart.ide.runner.server;

import java.util.StringTokenizer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.CommandLineTokenizer;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.RuntimeConfigurationError;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderImpl;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.ide.runner.DartConsoleFilter;
import com.jetbrains.lang.dart.ide.runner.base.DartRunConfigurationBase;
import com.jetbrains.lang.dart.util.DartSdkUtil;
import com.jetbrains.lang.dart.util.DartUrlResolver;

public class DartCommandLineRunningState extends CommandLineState
{
	protected final
	@NotNull
	DartCommandLineRunnerParameters myRunnerParameters;
	private int myDebuggingPort = -1;
	private Sdk mySdk;

	public DartCommandLineRunningState(final @NotNull ExecutionEnvironment env) throws ExecutionException
	{
		super(env);
		myRunnerParameters = ((DartRunConfigurationBase) env.getRunProfile()).getRunnerParameters();

		try
		{
			myRunnerParameters.check(env.getProject());
		}
		catch(RuntimeConfigurationError e)
		{
			throw new ExecutionException(e);
		}

		final TextConsoleBuilder builder = getConsoleBuilder();
		if(builder instanceof TextConsoleBuilderImpl)
		{
			((TextConsoleBuilderImpl) builder).setUsePredefinedMessageFilter(false);
		}

		try
		{
			mySdk = myRunnerParameters.getSdk(env.getProject());

			builder.addFilter(new DartConsoleFilter(env.getProject(), mySdk, myRunnerParameters.getDartFile()));
		}
		catch(RuntimeConfigurationError e)
		{
			builder.addFilter(new DartConsoleFilter(env.getProject(), null, null)); // can't happen because already checked
		}
	}

	public void setDebuggingPort(final int debuggingPort)
	{
		myDebuggingPort = debuggingPort;
	}

	@NotNull
	@Override
	protected ProcessHandler startProcess() throws ExecutionException
	{
		return doStartProcess(null);
	}

	protected ProcessHandler doStartProcess(final @Nullable String overriddenMainFilePath) throws ExecutionException
	{
		final GeneralCommandLine commandLine = createCommandLine(overriddenMainFilePath);
		final OSProcessHandler processHandler = new OSProcessHandler(commandLine);
		ProcessTerminatedListener.attach(processHandler, getEnvironment().getProject());
		return processHandler;
	}

	private GeneralCommandLine createCommandLine(final @Nullable String overriddenMainFilePath) throws ExecutionException
	{
		if(mySdk == null)
		{
			throw new ExecutionException(DartBundle.message("dart.sdk.is.not.configured"));
		}

		final String dartExePath = DartSdkUtil.getCompilerPathByFolderPath(mySdk.getHomePath());

		final VirtualFile dartFile;
		try
		{
			dartFile = myRunnerParameters.getDartFile();
		}
		catch(RuntimeConfigurationError e)
		{
			throw new ExecutionException(e); // can't happen because already checked
		}

		final String workingDir = StringUtil.isEmptyOrSpaces(myRunnerParameters.getWorkingDirectory()) ? dartFile.getParent().getPath() :
				myRunnerParameters.getWorkingDirectory();

		final GeneralCommandLine commandLine = new GeneralCommandLine();
		commandLine.setExePath(dartExePath);
		commandLine.setWorkDirectory(workingDir);
		commandLine.getEnvironment().putAll(myRunnerParameters.getEnvs());
		commandLine.setPassParentEnvironment(myRunnerParameters.isIncludeParentEnvs());
		setupParameters(getEnvironment().getProject(), commandLine, myRunnerParameters, myDebuggingPort, overriddenMainFilePath);

		return commandLine;
	}

	private static void setupParameters(final @NotNull Project project, final @NotNull GeneralCommandLine commandLine,
			final @NotNull DartCommandLineRunnerParameters runnerParameters, final int debuggingPort, final @Nullable String overriddenMainFilePath)
			throws ExecutionException
	{
		commandLine.addParameter("--ignore-unrecognized-flags");

		final String vmOptions = runnerParameters.getVMOptions();
		if(vmOptions != null)
		{
			final StringTokenizer vmOptionsTokenizer = new CommandLineTokenizer(vmOptions);
			while(vmOptionsTokenizer.hasMoreTokens())
			{
				commandLine.addParameter(vmOptionsTokenizer.nextToken());
			}
		}

		final VirtualFile dartFile;
		try
		{
			dartFile = runnerParameters.getDartFile();
		}
		catch(RuntimeConfigurationError e)
		{
			throw new ExecutionException(e);
		}

		final VirtualFile[] packageRoots = DartUrlResolver.getInstance(project, dartFile).getPackageRoots();
		if(packageRoots.length > 0)
		{
			// more than one package root is not supported by the [SDK]/bin/dart tool
			commandLine.addParameter("--package-root=" + packageRoots[0].getPath());
		}

		if(debuggingPort > 0)
		{
			commandLine.addParameter("--debug:" + debuggingPort);
		}

		commandLine.addParameter(overriddenMainFilePath == null ? dartFile.getPath() : overriddenMainFilePath);

		final String arguments = runnerParameters.getArguments();
		if(arguments != null)
		{
			StringTokenizer argumentsTokenizer = new CommandLineTokenizer(arguments);
			while(argumentsTokenizer.hasMoreTokens())
			{
				commandLine.addParameter(argumentsTokenizer.nextToken());
			}
		}
	}
}
