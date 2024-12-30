package consulo.dart.debugger.breakpoint;

import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.ide.runner.DartLineBreakpointType;
import consulo.annotation.component.ExtensionImpl;
import consulo.execution.debug.breakpoint.XLineBreakpointType;
import consulo.execution.debug.breakpoint.XLineBreakpointTypeResolver;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.fileType.FileType;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author VISTALL
 * @since 5/8/2016
 */
@ExtensionImpl
public class DartLineBreakpointTypeResolver implements XLineBreakpointTypeResolver {
  @Nullable
  @Override
  public XLineBreakpointType<?> resolveBreakpointType(@Nonnull Project project, @Nonnull VirtualFile virtualFile, int line) {
    return DartLineBreakpointType.getInstance();
  }

  @Nonnull
  @Override
  public FileType getFileType() {
    return DartFileType.INSTANCE;
  }
}
