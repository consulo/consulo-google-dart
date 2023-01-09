package com.jetbrains.lang.dart.psi.impl;

import com.jetbrains.lang.dart.psi.DartOperator;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import consulo.language.ast.ASTNode;

import javax.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
abstract public class AbstractDartOperator extends DartPsiCompositeElementImpl implements DartOperator {
  public AbstractDartOperator(@Nonnull ASTNode node) {
    super(node);
  }

  @Nonnull
  @Override
  public String getOperatorString() {
    return DartResolveUtil.getOperatorString(getUserDefinableOperator());
  }
}
