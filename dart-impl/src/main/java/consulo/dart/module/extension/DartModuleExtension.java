package consulo.dart.module.extension;

import org.jetbrains.annotations.NotNull;
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
	public DartModuleExtension(@NotNull String id, @NotNull ModuleRootLayer module)
	{
		super(id, module);
	}

	@NotNull
	@Override
	public Class<? extends SdkType> getSdkTypeClass()
	{
		return DartSdkType.class;
	}
}
