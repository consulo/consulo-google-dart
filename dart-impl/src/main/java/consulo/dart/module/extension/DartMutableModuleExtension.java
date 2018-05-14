package consulo.dart.module.extension;

import javax.swing.JComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.util.ui.JBUI;
import consulo.annotations.RequiredDispatchThread;
import consulo.extension.ui.ModuleExtensionSdkBoxBuilder;
import consulo.module.extension.MutableModuleExtensionWithSdk;
import consulo.module.extension.MutableModuleInheritableNamedPointer;
import consulo.roots.ModuleRootLayer;

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

	@RequiredDispatchThread
	@Nullable
	@Override
	public JComponent createConfigurablePanel(@Nullable Runnable runnable)
	{
		return JBUI.Panels.verticalPanel().addComponent(ModuleExtensionSdkBoxBuilder.createAndDefine(this, runnable).build());
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
