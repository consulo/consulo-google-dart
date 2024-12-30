package com.jetbrains.lang.dart;

import consulo.google.dart.icon.DartIconGroup;
import consulo.language.file.LanguageFileType;
import consulo.localize.LocalizeValue;
import consulo.ui.image.Image;

import jakarta.annotation.Nonnull;

/**
 * Created by IntelliJ IDEA.
 * User: Maxim.Mossienko
 * Date: 10/12/11
 * Time: 8:02 PM
 */
public class DartFileType extends LanguageFileType {
  public static final LanguageFileType INSTANCE = new DartFileType();
  public static final String DEFAULT_EXTENSION = "dart";

  private DartFileType() {
    super(DartLanguage.INSTANCE);
  }

  @Nonnull
  @Override
  public String getId() {
    return "Dart";
  }

  @Nonnull
  @Override
  public LocalizeValue getDescription() {
    return LocalizeValue.localizeTODO("Dart files");
  }

  @Nonnull
  @Override
  public String getDefaultExtension() {
    return DEFAULT_EXTENSION;
  }

  @Override
  public Image getIcon() {
    return DartIconGroup.dart_file();
  }
}
