package com.jetbrains.lang.dart.psi;

import consulo.language.psi.PsiNameIdentifierOwner;
import consulo.language.psi.PsiNamedElement;
import consulo.navigation.NavigationItem;

import javax.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
public interface DartQNamedElement extends DartPsiCompositeElement, PsiNamedElement, NavigationItem, PsiNameIdentifierOwner {
  @Nonnull
  DartId[] getIds();
}
