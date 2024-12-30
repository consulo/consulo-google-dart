package com.jetbrains.lang.dart.psi;

import com.jetbrains.lang.dart.DartLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.document.util.TextRange;
import consulo.language.inject.InjectedLanguagePlaces;
import consulo.language.inject.LanguageInjector;
import consulo.language.psi.PsiLanguageInjectionHost;

import jakarta.annotation.Nonnull;

@ExtensionImpl
public class DartLanguageInjector implements LanguageInjector {
  @Override
  public void injectLanguages(@Nonnull PsiLanguageInjectionHost host, @Nonnull InjectedLanguagePlaces injectionPlacesRegistrar) {
    if (host instanceof DartEmbeddedContent) {
      injectionPlacesRegistrar.addPlace(
        DartLanguage.INSTANCE,
        TextRange.create(0, host.getTextLength()),
        null,
        null
      );
    }
  }
}
