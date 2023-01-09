package com.jetbrains.lang.dart;

import consulo.language.DependentLanguage;
import consulo.language.Language;

public class DartInHtmlLanguage extends Language implements DependentLanguage {
  public static DartInHtmlLanguage INSTANCE = new DartInHtmlLanguage();

  private DartInHtmlLanguage() {
    super(DartLanguage.INSTANCE, "DART_IN_HTML");
  }
}
