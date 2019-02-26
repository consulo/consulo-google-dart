package com.jetbrains.lang.dart;

import com.intellij.lang.DependentLanguage;
import com.intellij.lang.Language;

public class DartInHtmlLanguage extends Language implements DependentLanguage {
  public static DartInHtmlLanguage INSTANCE = new DartInHtmlLanguage();

  private DartInHtmlLanguage() {
    super(DartLanguage.INSTANCE, "DART_IN_HTML");
  }
}
