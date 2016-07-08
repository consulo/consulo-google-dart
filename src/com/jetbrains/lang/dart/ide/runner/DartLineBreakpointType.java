package com.jetbrains.lang.dart.ide.runner;

import org.jetbrains.annotations.NotNull;
import com.intellij.xdebugger.breakpoints.XLineBreakpointTypeBase;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.ide.runner.base.DartDebuggerEditorsProvider;
import consulo.lombok.annotations.Lazy;

public class DartLineBreakpointType extends XLineBreakpointTypeBase
{
	@NotNull
	@Lazy
	public static DartLineBreakpointType getInstance()
	{
		return EXTENSION_POINT_NAME.findExtension(DartLineBreakpointType.class);
	}

	protected DartLineBreakpointType()
	{
		super("dart-line-breakpoint-type", DartBundle.message("dart.line.breakpoints.title"), new DartDebuggerEditorsProvider());
	}
}
