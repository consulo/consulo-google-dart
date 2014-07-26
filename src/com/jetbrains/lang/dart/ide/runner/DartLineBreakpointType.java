package com.jetbrains.lang.dart.ide.runner;

import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.breakpoints.XLineBreakpointTypeBase;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.ide.runner.base.DartDebuggerEditorsProvider;

public class DartLineBreakpointType extends XLineBreakpointTypeBase
{

	protected DartLineBreakpointType()
	{
		super("Dart", DartBundle.message("dart.line.breakpoints.title"), new DartDebuggerEditorsProvider());
	}

	@Override
	public boolean canPutAt(@NotNull final VirtualFile file, final int line, @NotNull final Project project)
	{
		return DartFileType.INSTANCE.equals(file.getFileType());
	}
}
