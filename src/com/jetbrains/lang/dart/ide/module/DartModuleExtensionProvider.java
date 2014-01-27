package com.jetbrains.lang.dart.ide.module;

import javax.swing.Icon;

import org.consulo.module.extension.ModuleExtensionProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.module.Module;
import icons.DartIcons;

/**
 * @author VISTALL
 * @since 18:42/08.06.13
 */
public class DartModuleExtensionProvider implements ModuleExtensionProvider<DartModuleExtension, DartMutableModuleExtension>
{
	@Nullable
	@Override
	public Icon getIcon()
	{
		return DartIcons.Dart;
	}

	@NotNull
	@Override
	public String getName()
	{
		return "Dart";
	}

	@NotNull
	@Override
	public DartModuleExtension createImmutable(@NotNull String s, @NotNull Module module)
	{
		return new DartModuleExtension(s, module);
	}

	@NotNull
	@Override
	public DartMutableModuleExtension createMutable(@NotNull String s, @NotNull Module module)
	{
		return new DartMutableModuleExtension(s, module);
	}
}
