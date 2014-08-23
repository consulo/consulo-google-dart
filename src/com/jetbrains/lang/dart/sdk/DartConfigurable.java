package com.jetbrains.lang.dart.sdk;

import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.LibraryOrderEntry;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.types.BinariesOrderRootType;
import com.intellij.openapi.vfs.VirtualFile;

public class DartConfigurable
{
	private static final String CUSTOM_PACKAGE_ROOTS_LIB_NAME = "Dart custom package roots";

	@NotNull
	public static VirtualFile[] getCustomPackageRoots(final @NotNull Module module)
	{
		for(OrderEntry entry : ModuleRootManager.getInstance(module).getOrderEntries())
		{
			if(entry instanceof LibraryOrderEntry && CUSTOM_PACKAGE_ROOTS_LIB_NAME.equals(((LibraryOrderEntry) entry).getLibraryName()))
			{
				return entry.getFiles(BinariesOrderRootType.getInstance());
			}
		}

		return VirtualFile.EMPTY_ARRAY;
	}

	public static boolean isCustomPackageRootSet(final @NotNull Module module)
	{
		for(OrderEntry entry : ModuleRootManager.getInstance(module).getOrderEntries())
		{
			if(entry instanceof LibraryOrderEntry && CUSTOM_PACKAGE_ROOTS_LIB_NAME.equals(((LibraryOrderEntry) entry).getLibraryName()))
			{
				return true;
			}
		}

		return false;
	}
}
