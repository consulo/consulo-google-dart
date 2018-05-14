package com.jetbrains.lang.dart.ide.runner;

import javax.annotation.Nonnull;

import com.intellij.xdebugger.breakpoints.XLineBreakpointTypeBase;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.ide.runner.base.DartDebuggerEditorsProvider;

public class DartLineBreakpointType extends XLineBreakpointTypeBase
{
	@Nonnull
	public static DartLineBreakpointType getInstance()
	{
		return EXTENSION_POINT_NAME.findExtension(DartLineBreakpointType.class);
	}

	protected DartLineBreakpointType()
	{
		super("dart-line-breakpoint-type", DartBundle.message("dart.line.breakpoints.title"), new DartDebuggerEditorsProvider());
	}
}
