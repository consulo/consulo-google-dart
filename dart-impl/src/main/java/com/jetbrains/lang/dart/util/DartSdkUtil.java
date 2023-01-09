package com.jetbrains.lang.dart.util;

import consulo.application.util.SystemInfo;
import consulo.content.base.BinariesOrderRootType;
import consulo.content.base.DocumentationOrderRootType;
import consulo.content.base.SourcesOrderRootType;
import consulo.content.bundle.SdkModificator;
import consulo.util.io.FileUtil;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.VirtualFileManager;
import consulo.virtualFileSystem.util.VirtualFileUtil;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Fedor.Korotkov
 */
public class DartSdkUtil {
  public static String getSdkVersion(String path) {
    try {
      return Files.readString(Path.of(path, "version")).trim();
    }
    catch (IOException e) {
      return "NA";
    }
  }

  @Nullable
  public static String getCompilerPathByFolderPath(@Nullable String folderPath) {
    return getExecutablePathByFolderPath(folderPath, "dart");
  }

  @Nullable
  private static String getExecutablePathByFolderPath(@Nullable String folderPath, String name) {
    if (folderPath == null) {
      return null;
    }
    final String folderUrl = VirtualFileUtil.pathToUrl(folderPath);
    final String candidate = folderUrl + "/bin/" + getExecutableName(name);
    if (fileExists(candidate)) {
      return FileUtil.toSystemIndependentName(VirtualFileUtil.urlToPath(candidate));
    }

    return null;
  }

  private static String getExecutableName(String name) {
    if (SystemInfo.isWindows) {
      return name + ".exe";
    }
    return name;
  }

  private static boolean fileExists(@Nullable String filePath) {
    return filePath != null && checkFileExists(VirtualFileManager.getInstance().findFileByUrl(filePath));
  }

  private static boolean checkFileExists(@Nullable VirtualFile file) {
    return file != null && file.exists();
  }

  public static void setupSdkPaths(@Nullable VirtualFile sdkRoot, SdkModificator modificator) {
    if (sdkRoot == null) {
      return;
    }
    final VirtualFile libRoot = sdkRoot.findChild("lib");
    if (libRoot != null) {
      for (VirtualFile child : libRoot.getChildren()) {
        if (!"html".equals(child.getName()) && !"_internal".equals(child.getName())) {
          modificator.addRoot(child, SourcesOrderRootType.getInstance());
          modificator.addRoot(child, BinariesOrderRootType.getInstance());
        }
      }
    }

    final VirtualFile docRoot = sdkRoot.findChild("doc");
    if (docRoot != null) {
      modificator.addRoot(docRoot, DocumentationOrderRootType.getInstance());
    }
  }
}
