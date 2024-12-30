package com.jetbrains.lang.dart.psi;

import consulo.navigation.NavigationItem;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author: Fedor.Korotkov
 */
public interface DartOperator extends DartPsiCompositeElement, NavigationItem {
  @Nullable
  DartFormalParameterList getFormalParameterList();

  @Nullable
  DartReturnType getReturnType();

  @Nullable
  DartUserDefinableOperator getUserDefinableOperator();

  @Nonnull
  String getOperatorString();
}
