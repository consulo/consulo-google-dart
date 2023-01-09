package com.jetbrains.lang.dart.sdk.listPackageDirs;

import consulo.content.library.LibraryType;
import consulo.content.library.NewLibraryConfiguration;
import consulo.content.library.PersistentLibraryKind;
import consulo.content.library.ui.LibraryEditorComponent;
import consulo.content.library.ui.LibraryPropertiesEditor;
import consulo.project.Project;
import consulo.ui.image.Image;
import consulo.virtualFileSystem.VirtualFile;
import com.jetbrains.lang.dart.DartIcons;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;

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
  public NewLibraryConfiguration createNewLibrary(@Nonnull final JComponent parentComponent,
                                                  @Nullable final VirtualFile contextDirectory,
                                                  @Nonnull final Project project) {
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
