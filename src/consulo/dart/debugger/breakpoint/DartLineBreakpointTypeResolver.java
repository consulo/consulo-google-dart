package consulo.dart.debugger.breakpoint;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.breakpoints.XLineBreakpointType;
import com.jetbrains.lang.dart.ide.runner.DartLineBreakpointType;
import consulo.xdebugger.breakpoints.XLineBreakpointTypeResolver;

/**
 * @author VISTALL
 * @since 5/8/2016
 */
public class DartLineBreakpointTypeResolver implements XLineBreakpointTypeResolver
{
	@Nullable
	@Override
	public XLineBreakpointType<?> resolveBreakpointType(@NotNull Project project, @NotNull VirtualFile virtualFile, int line)
	{
		return DartLineBreakpointType.getInstance();
	}
}
