package consulo.dart.module.extension;

import javax.swing.JComponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
	public DartMutableModuleExtension(@NotNull String id, @NotNull ModuleRootLayer module)
	{
		super(id, module);
	}

	@NotNull
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
	public boolean isModified(@NotNull DartModuleExtension extension)
	{
		return isModifiedImpl(extension);
	}
}
