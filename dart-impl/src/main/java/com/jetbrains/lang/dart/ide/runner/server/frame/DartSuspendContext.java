package com.jetbrains.lang.dart.ide.runner.server.frame;

import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineDebugProcess;
import com.jetbrains.lang.dart.ide.runner.server.google.VmCallFrame;
import consulo.execution.debug.frame.XExecutionStack;
import consulo.execution.debug.frame.XSuspendContext;

import javax.annotation.Nonnull;
import java.util.List;

public class DartSuspendContext extends XSuspendContext {
  private final
  @Nonnull
  DartExecutionStack myExecutionStack;

  public DartSuspendContext(final @Nonnull DartCommandLineDebugProcess debugProcess, final @Nonnull List<VmCallFrame> vmCallFrames) {
    myExecutionStack = new DartExecutionStack(debugProcess, vmCallFrames);
  }

  @Override
  @Nonnull
  public XExecutionStack getActiveExecutionStack() {
    return myExecutionStack;
  }
}
