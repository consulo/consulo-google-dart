package com.jetbrains.lang.dart.psi;

import javax.annotation.Nonnull;

import com.intellij.navigation.NavigationItem;

import javax.annotation.Nullable;

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
