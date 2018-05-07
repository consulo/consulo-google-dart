package com.jetbrains.lang.dart.sdk.listPackageDirs;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.libraries.LibraryType;
import com.intellij.openapi.roots.libraries.NewLibraryConfiguration;
import com.intellij.openapi.roots.libraries.PersistentLibraryKind;
import com.intellij.openapi.roots.libraries.ui.LibraryEditorComponent;
import com.intellij.openapi.roots.libraries.ui.LibraryPropertiesEditor;
import com.intellij.openapi.vfs.VirtualFile;
import consulo.awt.TargetAWT;
import icons.DartIcons;

public class DartListPackageDirsLibraryType extends LibraryType<DartListPackageDirsLibraryProperties>
{

	public static final PersistentLibraryKind<DartListPackageDirsLibraryProperties> LIBRARY_KIND = new
			PersistentLibraryKind<DartListPackageDirsLibraryProperties>("DartListPackageDirsLibraryType")
	{
		@Override
		@NotNull
		public DartListPackageDirsLibraryProperties createDefaultProperties()
		{
			return new DartListPackageDirsLibraryProperties();
		}
	};

	protected DartListPackageDirsLibraryType()
	{
		super(LIBRARY_KIND);
	}

	@Override
	@Nullable
	public String getCreateActionName()
	{
		return null;
	}

	@Override
	@Nullable
	public NewLibraryConfiguration createNewLibrary(@NotNull final JComponent parentComponent, @Nullable final VirtualFile contextDirectory,
			@NotNull final Project project)
	{
		return null;
	}

	@Override
	@Nullable
	public LibraryPropertiesEditor createPropertiesEditor(@NotNull final LibraryEditorComponent<DartListPackageDirsLibraryProperties>
			editorComponent)
	{
		return null;
	}

	@Override
	@Nullable
	public Icon getIcon()
	{
		return TargetAWT.to(DartIcons.Dart);
	}
}
