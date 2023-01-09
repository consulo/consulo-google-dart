package com.jetbrains.lang.dart;

import consulo.language.Language;

/**
 * User: Maxim.Mossienko
 * Date: 10/12/11
 * Time: 8:02 PM
 */
public class DartLanguage extends Language {
  public static final Language INSTANCE = new DartLanguage();

  private DartLanguage() {
    super("Dart", "application/dart");
  }
}
