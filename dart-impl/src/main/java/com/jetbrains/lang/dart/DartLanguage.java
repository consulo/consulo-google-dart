package com.jetbrains.lang.dart;

import consulo.google.dart.localize.DartLocalize;
import consulo.language.Language;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;

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

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return DartLocalize.dartTitle();
    }
}
