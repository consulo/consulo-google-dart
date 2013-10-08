package com.jetbrains.lang.dart;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.vfs.VirtualFile;

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
  public String getName() {
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
  public Icon getIcon() {
    return icons.DartIcons.Dart;
  }

  @Override
  public String getCharset(@NotNull VirtualFile virtualFile, byte[] bytes) {
    return null;
  }
}
