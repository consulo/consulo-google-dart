package com.jetbrains.lang.dart.ide.runner.server;

import com.jetbrains.lang.dart.ide.runner.DartConsoleFilter;
import com.jetbrains.lang.dart.ide.runner.base.DartRunConfigurationBase;
import com.jetbrains.lang.dart.util.DartSdkUtil;
import com.jetbrains.lang.dart.util.DartUrlResolver;
import consulo.content.bundle.Sdk;
import consulo.execution.configuration.CommandLineState;
import consulo.execution.configuration.RuntimeConfigurationError;
import consulo.execution.process.ProcessTerminatedListener;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.execution.ui.console.TextConsoleBuilder;
import consulo.execution.util.CommandLineTokenizer;
import consulo.google.dart.localize.DartLocalize;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandler;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.local.ProcessHandlerFactory;
import consulo.project.Project;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.StringTokenizer;

public class DartCommandLineRunningState extends CommandLineState {
  protected final
  @Nonnull
  DartCommandLineRunnerParameters myRunnerParameters;
  private int myDebuggingPort = -1;
  private Sdk mySdk;

  public DartCommandLineRunningState(final @Nonnull ExecutionEnvironment env) throws ExecutionException {
    super(env);
    myRunnerParameters = ((DartRunConfigurationBase)env.getRunProfile()).getRunnerParameters();

    try {
      myRunnerParameters.check(env.getProject());
    }
    catch (RuntimeConfigurationError e) {
      throw new ExecutionException(e);
    }

    final TextConsoleBuilder builder = getConsoleBuilder();
    builder.setUsePredefinedMessageFilter(false);

    try {
      mySdk = myRunnerParameters.getSdk(env.getProject());

      builder.addFilter(new DartConsoleFilter(env.getProject(), mySdk, myRunnerParameters.getDartFile()));
    }
    catch (RuntimeConfigurationError e) {
      builder.addFilter(new DartConsoleFilter(env.getProject(), null, null)); // can't happen because already checked
    }
  }

  public void setDebuggingPort(final int debuggingPort) {
    myDebuggingPort = debuggingPort;
  }

  @Nonnull
  @Override
  protected ProcessHandler startProcess() throws ExecutionException {
    return doStartProcess(null);
  }

  protected ProcessHandler doStartProcess(final @Nullable String overriddenMainFilePath) throws ExecutionException {
    final GeneralCommandLine commandLine = createCommandLine(overriddenMainFilePath);
    final ProcessHandler processHandler = ProcessHandlerFactory.getInstance().createProcessHandler(commandLine);
    ProcessTerminatedListener.attach(processHandler, getEnvironment().getProject());
    return processHandler;
  }

  private GeneralCommandLine createCommandLine(final @Nullable String overriddenMainFilePath) throws ExecutionException {
    if (mySdk == null) {
      throw new ExecutionException(DartLocalize.dartSdkIsNotConfigured().get());
    }

    final String dartExePath = DartSdkUtil.getCompilerPathByFolderPath(mySdk.getHomePath());

    final VirtualFile dartFile;
    try {
      dartFile = myRunnerParameters.getDartFile();
    }
    catch (RuntimeConfigurationError e) {
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

  private static void setupParameters(final @Nonnull Project project,
                                      final @Nonnull GeneralCommandLine commandLine,
                                      final @Nonnull DartCommandLineRunnerParameters runnerParameters,
                                      final int debuggingPort,
                                      final @Nullable String overriddenMainFilePath)
    throws ExecutionException {
    commandLine.addParameter("--ignore-unrecognized-flags");

    final String vmOptions = runnerParameters.getVMOptions();
    if (vmOptions != null) {
      final StringTokenizer vmOptionsTokenizer = new CommandLineTokenizer(vmOptions);
      while (vmOptionsTokenizer.hasMoreTokens()) {
        commandLine.addParameter(vmOptionsTokenizer.nextToken());
      }
    }

    final VirtualFile dartFile;
    try {
      dartFile = runnerParameters.getDartFile();
    }
    catch (RuntimeConfigurationError e) {
      throw new ExecutionException(e);
    }

    final VirtualFile[] packageRoots = DartUrlResolver.getInstance(project, dartFile).getPackageRoots();
    if (packageRoots.length > 0) {
      // more than one package root is not supported by the [SDK]/bin/dart tool
      commandLine.addParameter("--package-root=" + packageRoots[0].getPath());
    }

    if (debuggingPort > 0) {
      commandLine.addParameter("--debug:" + debuggingPort);
    }

    commandLine.addParameter(overriddenMainFilePath == null ? dartFile.getPath() : overriddenMainFilePath);

    final String arguments = runnerParameters.getArguments();
    if (arguments != null) {
      StringTokenizer argumentsTokenizer = new CommandLineTokenizer(arguments);
      while (argumentsTokenizer.hasMoreTokens()) {
        commandLine.addParameter(argumentsTokenizer.nextToken());
      }
    }
  }
}
