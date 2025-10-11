package com.jetbrains.lang.dart.ide.runner;

import com.jetbrains.lang.dart.ide.runner.base.DartDebuggerEditorsProvider;
import consulo.annotation.component.ExtensionImpl;
import consulo.execution.debug.breakpoint.XLineBreakpointTypeBase;
import consulo.google.dart.localize.DartLocalize;
import jakarta.annotation.Nonnull;

@ExtensionImpl
public class DartLineBreakpointType extends XLineBreakpointTypeBase {
  @Nonnull
  public static DartLineBreakpointType getInstance() {
    return EXTENSION_POINT_NAME.findExtensionOrFail(DartLineBreakpointType.class);
  }

  public DartLineBreakpointType() {
    super("dart-line-breakpoint-type", DartLocalize.dartLineBreakpointsTitle().get(), new DartDebuggerEditorsProvider());
  }
}
