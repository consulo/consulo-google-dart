package com.jetbrains.lang.dart.sdk;

import consulo.content.base.BinariesOrderRootType;
import consulo.module.Module;
import consulo.module.content.ModuleRootManager;
import consulo.module.content.layer.orderEntry.LibraryOrderEntry;
import consulo.module.content.layer.orderEntry.OrderEntry;
import consulo.virtualFileSystem.VirtualFile;

import jakarta.annotation.Nonnull;

public class DartConfigurable {
  private static final String CUSTOM_PACKAGE_ROOTS_LIB_NAME = "Dart custom package roots";

  @Nonnull
  public static VirtualFile[] getCustomPackageRoots(final @Nonnull Module module) {
    for (OrderEntry entry : ModuleRootManager.getInstance(module).getOrderEntries()) {
      if (entry instanceof LibraryOrderEntry && CUSTOM_PACKAGE_ROOTS_LIB_NAME.equals(((LibraryOrderEntry)entry).getLibraryName())) {
        return entry.getFiles(BinariesOrderRootType.getInstance());
      }
    }

    return VirtualFile.EMPTY_ARRAY;
  }

  public static boolean isCustomPackageRootSet(final @Nonnull Module module) {
    for (OrderEntry entry : ModuleRootManager.getInstance(module).getOrderEntries()) {
      if (entry instanceof LibraryOrderEntry && CUSTOM_PACKAGE_ROOTS_LIB_NAME.equals(((LibraryOrderEntry)entry).getLibraryName())) {
        return true;
      }
    }

    return false;
  }
}
