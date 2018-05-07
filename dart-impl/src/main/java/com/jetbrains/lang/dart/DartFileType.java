package com.jetbrains.lang.dart;

import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.vfs.VirtualFile;
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

  @NotNull
  @Override
  public String getId() {
    return "Dart";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "Dart files";
  }

  @NotNull
  @Override
  public String getDefaultExtension() {
    return DEFAULT_EXTENSION;
  }

  @Override
  public Image getIcon() {
    return icons.DartIcons.Dart;
  }
}
