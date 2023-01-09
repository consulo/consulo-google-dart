package com.jetbrains.lang.dart;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 09/01/2023
 */
@ExtensionImpl
public class DartHtmlParserDefinition extends DartParserDefinition {
  @Nonnull
  @Override
  public Language getLanguage() {
    return DartInHtmlLanguage.INSTANCE;
  }
}
