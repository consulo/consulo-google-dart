package consulo.dart.module.extension;

import javax.annotation.Nonnull;
import com.intellij.openapi.projectRoots.SdkType;
import com.jetbrains.lang.dart.ide.DartSdkType;
import consulo.module.extension.impl.ModuleExtensionWithSdkImpl;
import consulo.roots.ModuleRootLayer;

/**
 * @author VISTALL
 * @since 17:49/08.06.13
 */
public class DartModuleExtension extends ModuleExtensionWithSdkImpl<DartModuleExtension>
{
	public DartModuleExtension(@Nonnull String id, @Nonnull ModuleRootLayer module)
	{
		super(id, module);
	}

	@Nonnull
	@Override
	public Class<? extends SdkType> getSdkTypeClass()
	{
		return DartSdkType.class;
	}
}
