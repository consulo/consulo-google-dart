package com.jetbrains.lang.dart.ide.runner.server.frame;

import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineDebugProcess;
import com.jetbrains.lang.dart.ide.runner.server.google.VmCallFrame;
import com.jetbrains.lang.dart.ide.runner.server.google.VmCallback;
import com.jetbrains.lang.dart.ide.runner.server.google.VmResult;
import com.jetbrains.lang.dart.ide.runner.server.google.VmValue;
import consulo.execution.debug.XSourcePosition;
import consulo.execution.debug.evaluation.XDebuggerEvaluator;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.io.IOException;

class DartDebuggerEvaluator extends XDebuggerEvaluator {
  @Nonnull
  private final DartCommandLineDebugProcess myDebugProcess;
  @Nonnull
  private final VmCallFrame myVmCallFrame;

  public DartDebuggerEvaluator(final @Nonnull DartCommandLineDebugProcess debugProcess, final @Nonnull VmCallFrame vmCallFrame) {
    myDebugProcess = debugProcess;
    myVmCallFrame = vmCallFrame;
  }

  @Override
  public boolean isCodeFragmentEvaluationSupported() {
    return false;
  }

  @Override
  public void evaluate(@Nonnull final String expression, @Nonnull final XEvaluationCallback callback,
                       @Nullable final XSourcePosition expressionPosition) {
    try {
      myDebugProcess.getVmConnection()
                    .evaluateOnCallFrame(myVmCallFrame.getIsolate(), myVmCallFrame, expression, new VmCallback<VmValue>() {
                      @Override
                      public void handleResult(final VmResult<VmValue> result) {
                        if (result.isError()) {
                          callback.errorOccurred(result.getError());
                        }
                        else {
                          final VmValue vmValue = result.getResult();
                          callback.evaluated(new DartValue(myDebugProcess, vmValue));
                        }
                      }
                    });
    }
    catch (IOException e) {
      callback.errorOccurred(e.toString());
    }
  }
}
