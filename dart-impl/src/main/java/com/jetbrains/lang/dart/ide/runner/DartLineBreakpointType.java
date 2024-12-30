package com.jetbrains.lang.dart.ide.runner;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.ide.runner.base.DartDebuggerEditorsProvider;
import consulo.annotation.component.ExtensionImpl;
import consulo.execution.debug.breakpoint.XLineBreakpointTypeBase;

import jakarta.annotation.Nonnull;

@ExtensionImpl
public class DartLineBreakpointType extends XLineBreakpointTypeBase {
  @Nonnull
  public static DartLineBreakpointType getInstance() {
    return EXTENSION_POINT_NAME.findExtension(DartLineBreakpointType.class);
  }

  public DartLineBreakpointType() {
    super("dart-line-breakpoint-type", DartBundle.message("dart.line.breakpoints.title"), new DartDebuggerEditorsProvider());
  }
}
