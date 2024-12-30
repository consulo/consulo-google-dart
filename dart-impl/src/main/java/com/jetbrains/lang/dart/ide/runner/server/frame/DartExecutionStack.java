package com.jetbrains.lang.dart.ide.runner.server.frame;

import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineDebugProcess;
import com.jetbrains.lang.dart.ide.runner.server.google.VmCallFrame;
import consulo.execution.debug.frame.XExecutionStack;
import consulo.execution.debug.frame.XStackFrame;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DartExecutionStack extends XExecutionStack {
  private final
  @Nonnull
  DartCommandLineDebugProcess myDebugProcess;
  private
  @Nullable
  DartStackFrame myTopFrame;
  private final
  @Nonnull
  List<VmCallFrame> myVmCallFrames;

  public DartExecutionStack(final @Nonnull DartCommandLineDebugProcess debugProcess, final @Nonnull List<VmCallFrame> vmCallFrames) {
    super("");
    myDebugProcess = debugProcess;
    myTopFrame = vmCallFrames.isEmpty() ? null : new DartStackFrame(myDebugProcess, vmCallFrames.get(0));
    myVmCallFrames = vmCallFrames;
  }

  @Override
  @Nullable
  public XStackFrame getTopFrame() {
    return myTopFrame;
  }

  @Override
  public void computeStackFrames(final XStackFrameContainer container) {
    final Iterator<VmCallFrame> iterator = myVmCallFrames.iterator();
    if (iterator.hasNext()) {
      iterator.next(); // skip top frame
    }

    while (iterator.hasNext()) {
      final VmCallFrame frame = iterator.next();
      container.addStackFrames(Collections.singletonList(new DartStackFrame(myDebugProcess, frame)), false);
    }

    container.addStackFrames(Collections.<XStackFrame>emptyList(), true);
  }
}
