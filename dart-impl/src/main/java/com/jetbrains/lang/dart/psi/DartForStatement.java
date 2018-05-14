// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi;

import java.util.List;

import javax.annotation.*;

public interface DartForStatement extends DartPsiCompositeElement {

  @Nullable
  DartAssertStatement getAssertStatement();

  @Nullable
  DartBlock getBlock();

  @Nullable
  DartBreakStatement getBreakStatement();

  @Nullable
  DartContinueStatement getContinueStatement();

  @Nullable
  DartDoWhileStatement getDoWhileStatement();

  @Nullable
  DartExpression getExpression();

  @Nullable
  DartForLoopPartsInBraces getForLoopPartsInBraces();

  @Nullable
  DartForStatement getForStatement();

  @Nullable
  DartFunctionDeclarationWithBody getFunctionDeclarationWithBody();

  @Nullable
  DartIfStatement getIfStatement();

  @Nonnull
  List<DartLabel> getLabelList();

  @Nullable
  DartRethrowStatement getRethrowStatement();

  @Nullable
  DartReturnStatement getReturnStatement();

  @Nullable
  DartSwitchStatement getSwitchStatement();

  @Nullable
  DartThrowStatement getThrowStatement();

  @Nullable
  DartTryStatement getTryStatement();

  @Nullable
  DartVarDeclarationList getVarDeclarationList();

  @Nullable
  DartWhileStatement getWhileStatement();

}
