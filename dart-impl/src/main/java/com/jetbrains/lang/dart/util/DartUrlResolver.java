package com.jetbrains.lang.dart.util;

import com.jetbrains.lang.dart.ide.index.DartLibraryIndex;
import consulo.content.bundle.Sdk;
import consulo.project.Project;
import consulo.util.lang.function.PairConsumer;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class DartUrlResolver {

  public static final String DART_SCHEME = "dart";
  public static final String DART_PREFIX = "dart:";
  public static final String PACKAGE_SCHEME = "package";
  public static final String PACKAGE_PREFIX = "package:";
  public static final String FILE_SCHEME = "file";
  public static final String FILE_PREFIX = "file:";
  public static final String PACKAGES_FOLDER_NAME = "packages";

  /**
   * Returned instance becomes obsolete if/when pubspec.yaml file is added or deleted or if module-specific custom package roots are changed,
   * so do not keep returned instance too long.
   *
   * @param project
   * @param contextFile may be pubspec.yaml file, its parent folder or any file/folder within this parent folder; in case of import statements
   *                    resolve this must be an analyzed file
   * @return
   */
  @Nonnull
  public static DartUrlResolver getInstance(final @Nonnull Project project, final @Nonnull VirtualFile contextFile) {
    return new DartUrlResolverImpl(project, contextFile);
  }

  @Nullable
  public abstract VirtualFile getPubspecYamlFile();

  @Nonnull
  public abstract VirtualFile[] getPackageRoots();

  /**
   * Process 'Path Packages' (https://www.dartlang.org/tools/pub/dependencies.html#path-packages) and this package itself (symlink to local 'lib'
   * folder)
   */
  public abstract void processLivePackages(final @Nonnull PairConsumer<String, VirtualFile> packageNameAndDirConsumer);

  @Nullable
  public abstract VirtualFile getPackageDirIfLivePackageOrFromPubListPackageDirs(final @Nonnull String packageName);

  /**
   * Dart url has <code>dart:</code>, <code>package:</code> or <code>file:</code> scheme
   */
  @Nullable
  public abstract VirtualFile findFileByDartUrl(@Nonnull String url);

  @Nullable
  public static VirtualFile findFileInDartSdkLibFolder(final @Nonnull Project project, final @Nullable Sdk dartSdk,
                                                       final @Nullable String dartUrl) {
    if (dartSdk == null || dartUrl == null || !dartUrl.startsWith(DART_PREFIX)) {
      return null;
    }

    final String sdkLibNameOrRelPath = dartUrl.substring(DART_PREFIX.length());
    final VirtualFile sdkLibByName = DartLibraryIndex.getStandardLibraryFromSdk(project, dartSdk, sdkLibNameOrRelPath);

    if (sdkLibByName != null) {
      return sdkLibByName;
    }

    final String path = dartSdk.getHomePath() + "/lib/" + sdkLibNameOrRelPath;
    return LocalFileSystem.getInstance().findFileByPath(path);
  }

  @Nonnull
  public abstract String getDartUrlForFile(final @Nonnull VirtualFile file);
}
