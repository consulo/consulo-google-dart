package com.jetbrains.lang.dart.sdk.listPackageDirs;

import consulo.content.library.LibraryType;
import consulo.content.library.PersistentLibraryKind;
import consulo.content.library.ui.LibraryEditorComponent;
import consulo.content.library.ui.LibraryPropertiesEditor;
import consulo.ui.image.Image;
import com.jetbrains.lang.dart.DartIcons;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class DartListPackageDirsLibraryType extends LibraryType<DartListPackageDirsLibraryProperties> {

  public static final PersistentLibraryKind<DartListPackageDirsLibraryProperties> LIBRARY_KIND =
    new PersistentLibraryKind<DartListPackageDirsLibraryProperties>("DartListPackageDirsLibraryType") {
      @Override
      @Nonnull
      public DartListPackageDirsLibraryProperties createDefaultProperties() {
        return new DartListPackageDirsLibraryProperties();
      }
    };

  protected DartListPackageDirsLibraryType() {
    super(LIBRARY_KIND);
  }

  @Override
  @Nullable
  public String getCreateActionName() {
    return null;
  }

  @Override
  @Nullable
  public LibraryPropertiesEditor createPropertiesEditor(@Nonnull final LibraryEditorComponent<DartListPackageDirsLibraryProperties> editorComponent) {
    return null;
  }

  @Override
  @Nullable
  public Image getIcon() {
    return DartIcons.Dart;
  }
}
