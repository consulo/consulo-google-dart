package com.jetbrains.lang.dart.highlight;

import com.jetbrains.lang.dart.DartInHtmlLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 09/01/2023
 */
@ExtensionImpl
public class DartHtmlSyntaxHighlighterFactory extends DartSyntaxHighlighterFactory {
  @Nonnull
  @Override
  public Language getLanguage() {
    return DartInHtmlLanguage.INSTANCE;
  }
}
