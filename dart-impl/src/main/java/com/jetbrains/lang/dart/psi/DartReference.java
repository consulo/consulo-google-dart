package com.jetbrains.lang.dart.psi;

import com.intellij.psi.PsiReference;
import com.jetbrains.lang.dart.util.DartClassResolveResult;
import javax.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
public interface DartReference extends DartExpression, PsiReference {
  @Nonnull
  DartClassResolveResult resolveDartClass();
}
