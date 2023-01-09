package com.jetbrains.lang.dart.ide.runner;

import com.jetbrains.lang.dart.ide.runner.base.DartRunConfigurationBase;
import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineDebugProcess;
import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineRunningState;
import consulo.annotation.component.ExtensionImpl;
import consulo.document.FileDocumentManager;
import consulo.execution.ExecutionResult;
import consulo.execution.configuration.RunProfile;
import consulo.execution.configuration.RunProfileState;
import consulo.execution.configuration.RuntimeConfigurationError;
import consulo.execution.debug.*;
import consulo.execution.executor.DefaultRunExecutor;
import consulo.execution.runner.DefaultProgramRunner;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.execution.ui.RunContentDescriptor;
import consulo.logging.Logger;
import consulo.process.ExecutionException;
import consulo.util.io.NetUtil;
import consulo.virtualFileSystem.VirtualFile;

import javax.annotation.Nonnull;

@ExtensionImpl
public class DartRunner extends DefaultProgramRunner {

  private static final Logger LOG = Logger.getInstance(DartRunner.class.getName());

  @Nonnull
  @Override
  public String getRunnerId() {
    return "DartRunner";
  }

  @Override
  public boolean canRun(final @Nonnull String executorId, final @Nonnull RunProfile profile) {
    return profile instanceof DartRunConfigurationBase && (DefaultRunExecutor.EXECUTOR_ID.equals(executorId) || DefaultDebugExecutor.EXECUTOR_ID
      .equals(executorId));
  }

  protected RunContentDescriptor doExecute(final @Nonnull RunProfileState state,
                                           final @Nonnull ExecutionEnvironment env) throws ExecutionException {
    final String executorId = env.getExecutor().getId();

    if (DefaultRunExecutor.EXECUTOR_ID.equals(executorId)) {
      return super.doExecute(state, env);
    }

    if (DefaultDebugExecutor.EXECUTOR_ID.equals(executorId)) {
      try {
        return doExecuteDartDebug(state, env);
      }
      catch (RuntimeConfigurationError e) {
        throw new ExecutionException(e);
      }
    }

    LOG.error("Unexpected executor id: " + executorId);
    return null;
  }

  private RunContentDescriptor doExecuteDartDebug(final @Nonnull RunProfileState state,
                                                  final @Nonnull ExecutionEnvironment env) throws RuntimeConfigurationError, ExecutionException {
    FileDocumentManager.getInstance().saveAllDocuments();

    final DartRunConfigurationBase configuration = (DartRunConfigurationBase)env.getRunProfile();
    final VirtualFile mainDartFile = configuration.getRunnerParameters().getDartFile();

    final int debuggingPort = NetUtil.tryToFindAvailableSocketPort();
    ((DartCommandLineRunningState)state).setDebuggingPort(debuggingPort);
    final ExecutionResult executionResult = state.execute(env.getExecutor(), this);
    if (executionResult == null) {
      return null;
    }

    final XDebuggerManager debuggerManager = XDebuggerManager.getInstance(env.getProject());
    final XDebugSession debugSession = debuggerManager.startSession(env, new XDebugProcessStarter() {
      @Override
      @Nonnull
      public XDebugProcess start(@Nonnull final XDebugSession session) throws ExecutionException {
        return new DartCommandLineDebugProcess(session, debuggingPort, executionResult, mainDartFile);
      }
    });

    return debugSession.getRunContentDescriptor();
  }
}
