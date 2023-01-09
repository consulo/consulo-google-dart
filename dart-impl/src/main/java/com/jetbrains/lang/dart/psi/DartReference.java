package com.jetbrains.lang.dart.psi;

import com.jetbrains.lang.dart.util.DartClassResolveResult;
import consulo.language.psi.PsiReference;

import javax.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
public interface DartReference extends DartExpression, PsiReference {
  @Nonnull
  DartClassResolveResult resolveDartClass();
}
