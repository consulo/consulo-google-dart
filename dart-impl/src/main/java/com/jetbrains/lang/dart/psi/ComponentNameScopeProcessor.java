package com.jetbrains.lang.dart.psi;

import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import consulo.util.dataholder.Key;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Set;

/**
 * @author: Fedor.Korotkov
 */
public class ComponentNameScopeProcessor implements PsiScopeProcessor {
  private final Set<DartComponentName> result;

  public ComponentNameScopeProcessor(Set<DartComponentName> result) {
    this.result = result;
  }

  @Override
  public boolean execute(@Nonnull PsiElement element, ResolveState state) {
    if (element instanceof DartComponentName) {
      result.add((DartComponentName)element);
    }
    return true;
  }

  @Override
  public <T> T getHint(@Nonnull Key<T> hintKey) {
    return null;
  }

  @Override
  public void handleEvent(Event event, @Nullable Object associated) {
  }
}
