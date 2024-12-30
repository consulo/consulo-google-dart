package com.jetbrains.lang.dart.ide.runner.unittest;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.ide.runner.DartConsoleFilter;
import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineRunningState;
import consulo.application.util.SystemInfo;
import consulo.content.bundle.Sdk;
import consulo.disposer.Disposer;
import consulo.execution.DefaultExecutionResult;
import consulo.execution.ExecutionResult;
import consulo.execution.configuration.RuntimeConfigurationError;
import consulo.execution.executor.Executor;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.execution.runner.ProgramRunner;
import consulo.execution.test.TestConsoleProperties;
import consulo.execution.test.action.ToggleAutoTestAction;
import consulo.execution.test.sm.SMTestRunnerConnectionUtil;
import consulo.execution.test.sm.runner.SMTRunnerConsoleProperties;
import consulo.execution.test.sm.ui.SMTRunnerConsoleView;
import consulo.execution.ui.console.ConsoleView;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandler;
import consulo.project.Project;
import consulo.util.io.FileUtil;
import consulo.util.io.ResourceUtil;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.util.VirtualFileUtil;

import jakarta.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class DartUnitRunningState extends DartCommandLineRunningState {
  private static final String DART_FRAMEWORK_NAME = "DartTestRunner";
  private static final String UNIT_CONFIG_FILE_NAME = "jetbrains_unit_config.dart";

  public DartUnitRunningState(final @Nonnull ExecutionEnvironment environment) throws ExecutionException {
    super(environment);
  }

  @Override
  @Nonnull
  public ExecutionResult execute(final @Nonnull Executor executor, final @Nonnull ProgramRunner runner) throws ExecutionException {
    final ProcessHandler processHandler = startProcess();
    final ConsoleView consoleView = createConsole(getEnvironment());
    consoleView.attachToProcess(processHandler);

    final DefaultExecutionResult executionResult = new DefaultExecutionResult(consoleView, processHandler);
    executionResult.setRestartActions(new ToggleAutoTestAction());
    return executionResult;
  }

  private static ConsoleView createConsole(@Nonnull ExecutionEnvironment env) throws ExecutionException {
    final Project project = env.getProject();
    final DartUnitRunConfiguration runConfiguration = (DartUnitRunConfiguration)env.getRunProfile();
    final DartUnitRunnerParameters runnerParameters = runConfiguration.getRunnerParameters();

    VirtualFile dartFile = null;
    Sdk sdk = null;
    try {
      dartFile = runnerParameters.getDartFile();
      sdk = runnerParameters.getSdk(project);
    }
    catch (RuntimeConfigurationError ignore) {/**/}

    final DartConsoleFilter filter = new DartConsoleFilter(project, sdk, dartFile);

    final TestConsoleProperties testConsoleProperties =
      new SMTRunnerConsoleProperties(runConfiguration, DART_FRAMEWORK_NAME, env.getExecutor());
    testConsoleProperties.setUsePredefinedMessageFilter(false);

    final SMTRunnerConsoleView smtConsoleView = SMTestRunnerConnectionUtil.createConsoleWithCustomLocator(DART_FRAMEWORK_NAME,
                                                                                                          testConsoleProperties,
                                                                                                          env,
                                                                                                          new DartTestLocationProvider(),
                                                                                                          true,
                                                                                                          null);

    smtConsoleView.addMessageFilter(filter);

    Disposer.register(project, smtConsoleView);
    return smtConsoleView;
  }

  @Nonnull
  @Override
  protected ProcessHandler startProcess() throws ExecutionException {
    final String testRunnerPath;
    try {
      testRunnerPath = createTestRunnerFile();
    }
    catch (IOException e) {
      throw new ExecutionException(DartBundle.message("failed.to.create.test.runner", e.getMessage()));
    }

    return doStartProcess(testRunnerPath);
  }

  private String createTestRunnerFile() throws IOException {
    final File file = new File(FileUtil.getTempDirectory(), UNIT_CONFIG_FILE_NAME);
    if (!file.exists()) {
      //noinspection ResultOfMethodCallIgnored
      file.createNewFile();
    }

    final DartUnitRunnerParameters.Scope scope = ((DartUnitRunnerParameters)myRunnerParameters).getScope();
    final String name = ((DartUnitRunnerParameters)myRunnerParameters).getTestName();

    String runnerCode = getRunnerCode();
    runnerCode = runnerCode.replaceFirst("DART_UNITTEST", "package:unittest/unittest.dart");
    runnerCode = runnerCode.replaceFirst("NAME", StringUtil.notNullize(name));
    runnerCode = runnerCode.replaceFirst("SCOPE", scope.toString());
    final String filePath = myRunnerParameters.getFilePath();
    runnerCode = runnerCode.replaceFirst("TEST_FILE_PATH", filePath == null ? "" : pathToDartUrl(filePath));

    FileUtil.writeToFile(file, runnerCode);

    return file.getAbsolutePath();
  }

  private static String pathToDartUrl(@Nonnull String path) {
    final String url = VirtualFileUtil.pathToUrl(path);
    return SystemInfo.isWindows ? url.replace("file://", "file:///") : url;
  }

  private static String getRunnerCode() throws IOException {
    final URL resource = ResourceUtil.getResource(DartUnitRunningState.class, "/config", UNIT_CONFIG_FILE_NAME);
    return ResourceUtil.loadText(resource);
  }
}
