package com.jetbrains.lang.dart.analyzer;

import com.jetbrains.lang.dart.DartInHtmlLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 09/01/2023
 */
@ExtensionImpl
public class DartHtmlInProcessAnnotator extends DartInProcessAnnotator {
  @Nonnull
  @Override
  public Language getLanguage() {
    return DartInHtmlLanguage.INSTANCE;
  }
}
