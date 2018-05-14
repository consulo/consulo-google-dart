// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi;

import java.util.List;

import javax.annotation.*;

public interface DartCompareExpression extends DartExpression, DartReference {

  @Nullable
  DartEqualityOperator getEqualityOperator();

  @Nonnull
  List<DartExpression> getExpressionList();

  @Nullable
  DartRelationalOperator getRelationalOperator();

}
