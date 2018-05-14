// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi;

import java.util.List;

import javax.annotation.*;

public interface DartTryStatement extends DartPsiCompositeElement {

  @Nullable
  DartBlock getBlock();

  @Nullable
  DartFinallyPart getFinallyPart();

  @Nonnull
  List<DartOnPart> getOnPartList();

}
