package com.jetbrains.lang.dart.ide.module;

import org.consulo.module.extension.impl.ModuleExtensionWithSdkImpl;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.jetbrains.lang.dart.ide.DartSdkType;

/**
 * @author VISTALL
 * @since 17:49/08.06.13
 */
public class DartModuleExtension extends ModuleExtensionWithSdkImpl<DartModuleExtension>
{
	public DartModuleExtension(@NotNull String id, @NotNull ModifiableRootModel module)
	{
		super(id, module);
	}

	@Override
	protected Class<? extends SdkType> getSdkTypeClass()
	{
		return DartSdkType.class;
	}
}
