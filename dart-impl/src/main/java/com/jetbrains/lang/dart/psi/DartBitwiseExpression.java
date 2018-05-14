// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi;

import java.util.List;

import javax.annotation.Nonnull;

public interface DartBitwiseExpression extends DartExpression, DartReference {

  @Nonnull
  DartBitwiseOperator getBitwiseOperator();

  @Nonnull
  List<DartExpression> getExpressionList();

}
