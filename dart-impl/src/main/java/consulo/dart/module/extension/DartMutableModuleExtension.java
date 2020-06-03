package consulo.dart.module.extension;

import com.intellij.openapi.projectRoots.Sdk;
import consulo.disposer.Disposable;
import consulo.extension.ui.ModuleExtensionBundleBoxBuilder;
import consulo.module.extension.MutableModuleExtensionWithSdk;
import consulo.module.extension.MutableModuleInheritableNamedPointer;
import consulo.roots.ModuleRootLayer;
import consulo.ui.Component;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.layout.VerticalLayout;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 17:50/08.06.13
 */
public class DartMutableModuleExtension extends DartModuleExtension implements MutableModuleExtensionWithSdk<DartModuleExtension>
{
	public DartMutableModuleExtension(@Nonnull String id, @Nonnull ModuleRootLayer module)
	{
		super(id, module);
	}

	@Nonnull
	@Override
	public MutableModuleInheritableNamedPointer<Sdk> getInheritableSdk()
	{
		return (MutableModuleInheritableNamedPointer<Sdk>) super.getInheritableSdk();
	}

	@RequiredUIAccess
	@Nullable
	@Override
	public Component createConfigurationComponent(@Nonnull Disposable disposable, @Nonnull Runnable runnable)
	{
		VerticalLayout layout = VerticalLayout.create();
		layout.add(ModuleExtensionBundleBoxBuilder.createAndDefine(this, disposable, runnable).build());
		return layout;
	}

	@Override
	public void setEnabled(boolean b)
	{
		myIsEnabled = b;
	}

	@Override
	public boolean isModified(@Nonnull DartModuleExtension extension)
	{
		return isModifiedImpl(extension);
	}
}
