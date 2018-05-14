package com.jetbrains.lang.dart;

import javax.annotation.Nonnull;
import com.intellij.openapi.fileTypes.LanguageFileType;
import consulo.ui.image.Image;

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
  public String getDescription() {
    return "Dart files";
  }

  @Nonnull
  @Override
  public String getDefaultExtension() {
    return DEFAULT_EXTENSION;
  }

  @Override
  public Image getIcon() {
    return icons.DartIcons.Dart;
  }
}
