package com.jetbrains.lang.dart.ide.runner.server.frame;

import java.util.List;

import javax.annotation.Nonnull;

import com.intellij.xdebugger.frame.XExecutionStack;
import com.intellij.xdebugger.frame.XSuspendContext;
import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineDebugProcess;
import com.jetbrains.lang.dart.ide.runner.server.google.VmCallFrame;

public class DartSuspendContext extends XSuspendContext
{
	private final
	@Nonnull
	DartExecutionStack myExecutionStack;

	public DartSuspendContext(final @Nonnull DartCommandLineDebugProcess debugProcess, final @Nonnull List<VmCallFrame> vmCallFrames)
	{
		myExecutionStack = new DartExecutionStack(debugProcess, vmCallFrames);
	}

	@Override
	@Nonnull
	public XExecutionStack getActiveExecutionStack()
	{
		return myExecutionStack;
	}
}
