package com.jetbrains.lang.dart.ide.runner;

import org.consulo.lombok.annotations.LazyInstance;
import org.jetbrains.annotations.NotNull;
import com.intellij.xdebugger.breakpoints.XLineBreakpointTypeBase;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.ide.runner.base.DartDebuggerEditorsProvider;

public class DartLineBreakpointType extends XLineBreakpointTypeBase
{
	@NotNull
	@LazyInstance
	public static DartLineBreakpointType getInstance()
	{
		return EXTENSION_POINT_NAME.findExtension(DartLineBreakpointType.class);
	}

	protected DartLineBreakpointType()
	{
		super("dart-line-breakpoint-type", DartBundle.message("dart.line.breakpoints.title"), new DartDebuggerEditorsProvider());
	}
}
