package com.jetbrains.lang.dart.psi;

import consulo.language.ast.IElementType;
import consulo.language.psi.NavigatablePsiElement;

/**
 * @author: Fedor.Korotkov
 */
public interface DartPsiCompositeElement extends NavigatablePsiElement {
  IElementType getTokenType();
}
