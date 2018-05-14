// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi;

import java.util.List;

import javax.annotation.*;

public interface DartSwitchCase extends DartPsiCompositeElement {

  @Nonnull
  List<DartExpression> getExpressionList();

  @Nullable
  DartLabel getLabel();

  @Nonnull
  DartStatements getStatements();

}
