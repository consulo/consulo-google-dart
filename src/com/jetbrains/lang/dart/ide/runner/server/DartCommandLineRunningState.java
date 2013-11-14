package com.jetbrains.lang.dart.ide.runner.server;

import org.jetbrains.annotations.NotNull;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.util.text.StringTokenizer;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.ide.runner.DartStackTraceMessageFilter;
import com.jetbrains.lang.dart.ide.settings.DartSdkUtil;
import com.jetbrains.lang.dart.util.DartResolveUtil;

/**
 * @author: Fedor.Korotkov
 */
public class DartCommandLineRunningState extends CommandLineState {
  private final Module module;
  private final @NotNull String filePath;
  private final @NotNull String vmOptions;
  private final @NotNull String arguments;

  public DartCommandLineRunningState(ExecutionEnvironment env,
                                     Module module,
                                     @NotNull String filePath,
                                     @NotNull String vmOptions,
                                     @NotNull String arguments) {
    super(env);
    this.module = module;
    this.filePath = filePath;
    this.vmOptions = vmOptions;
    this.arguments = arguments;
  }

  @NotNull
  @Override
  protected ProcessHandler startProcess() throws ExecutionException {
    GeneralCommandLine commandLine = getCommand();

    return new OSProcessHandler(commandLine.createProcess(), commandLine.getCommandLineString());
  }

  public GeneralCommandLine getCommand() throws ExecutionException {
    Sdk dartSdkUtil = DartSdkUtil.getSdkForModule(module);
    String dartExecutablePath = com.jetbrains.lang.dart.util.DartSdkUtil
      .getCompilerPathByFolderPath(dartSdkUtil != null ? dartSdkUtil.getHomePath() : null);
    if (dartSdkUtil == null || dartExecutablePath == null) {
      throw new ExecutionException(DartBundle.message("bad.home.for.sdk", module.getName()));
    }
    final GeneralCommandLine commandLine = new GeneralCommandLine();

    commandLine.setExePath(dartExecutablePath);
    commandLine.setWorkDirectory(module.getModuleDirPath());
    commandLine.setPassParentEnvironment(true);

    setupUserProperties(
      module,
      commandLine,
      dartSdkUtil
    );

    final TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(module.getProject());
    consoleBuilder.addFilter(new DartStackTraceMessageFilter(module.getProject(), filePath));
    setConsoleBuilder(consoleBuilder);

    return commandLine;
  }

  private void setupUserProperties(@NotNull Module module,
                                   GeneralCommandLine commandLine,
                                   @NotNull Sdk sdk) {
    commandLine.getEnvironment().put("com.google.dart.sdk", sdk.getHomePath());

    commandLine.addParameter("--ignore-unrecognized-flags");

    StringTokenizer argumentsTokenizer = new StringTokenizer(vmOptions);
    while (argumentsTokenizer.hasMoreTokens()) {
      commandLine.addParameter(argumentsTokenizer.nextToken());
    }

    String libUrl = VfsUtilCore.pathToUrl(filePath);
    final VirtualFile libraryRoot = VirtualFileManager.getInstance().findFileByUrl(libUrl);
    final VirtualFile packages = DartResolveUtil.getDartPackagesFolder(module.getProject(), libraryRoot);
    if (packages != null && packages.isDirectory()) {
      commandLine.addParameter("--package-root=" + packages.getPath() + "/");
    }

    commandLine.addParameter(filePath);

    argumentsTokenizer = new StringTokenizer(arguments);
    while (argumentsTokenizer.hasMoreTokens()) {
      commandLine.addParameter(argumentsTokenizer.nextToken());
    }
  }
}
