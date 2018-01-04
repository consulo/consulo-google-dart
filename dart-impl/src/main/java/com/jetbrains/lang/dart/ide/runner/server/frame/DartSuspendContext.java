package com.jetbrains.lang.dart.ide.runner.server.frame;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import com.intellij.xdebugger.frame.XExecutionStack;
import com.intellij.xdebugger.frame.XSuspendContext;
import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineDebugProcess;
import com.jetbrains.lang.dart.ide.runner.server.google.VmCallFrame;

public class DartSuspendContext extends XSuspendContext
{
	private final
	@NotNull
	DartExecutionStack myExecutionStack;

	public DartSuspendContext(final @NotNull DartCommandLineDebugProcess debugProcess, final @NotNull List<VmCallFrame> vmCallFrames)
	{
		myExecutionStack = new DartExecutionStack(debugProcess, vmCallFrames);
	}

	@Override
	@NotNull
	public XExecutionStack getActiveExecutionStack()
	{
		return myExecutionStack;
	}
}
