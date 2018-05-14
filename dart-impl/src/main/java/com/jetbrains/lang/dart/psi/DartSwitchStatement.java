// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi;

import java.util.List;

import javax.annotation.*;

public interface DartSwitchStatement extends DartPsiCompositeElement {

  @Nullable
  DartDefaultCase getDefaultCase();

  @Nullable
  DartExpression getExpression();

  @Nonnull
  List<DartSwitchCase> getSwitchCaseList();

}
