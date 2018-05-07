package com.jetbrains.lang.dart.ide;

import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.PathChooserDialog;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.util.SystemInfo;
import com.jetbrains.lang.dart.util.DartSdkUtil;
import consulo.roots.types.BinariesOrderRootType;
import consulo.roots.types.DocumentationOrderRootType;
import consulo.roots.types.SourcesOrderRootType;
import consulo.ui.image.Image;

/**
 * @author Fedor.Korotkov
 */
public class DartSdkType extends SdkType
{
	public static DartSdkType getInstance()
	{
		return SdkType.EP_NAME.findExtension(DartSdkType.class);
	}

	public DartSdkType()
	{
		super("Dart SDK");
	}

	@Override
	public Image getIcon()
	{
		return icons.DartIcons.Dart;
	}

	@NotNull
	@Override
	public String getPresentableName()
	{
		return "Dart";
	}

	@Override
	public String suggestSdkName(String currentSdkName, String sdkHome)
	{
		return "Dart " + getVersionString(sdkHome);
	}

	@Override
	public String getVersionString(String sdkHome)
	{
		return DartSdkUtil.getSdkVersion(sdkHome);
	}

	@Override
	public boolean isValidSdkHome(String path)
	{
		return DartSdkUtil.getCompilerPathByFolderPath(path) != null;
	}

	@Override
	public boolean isRootTypeApplicable(OrderRootType type)
	{
		return type == BinariesOrderRootType.getInstance() || type == SourcesOrderRootType.getInstance() || type == DocumentationOrderRootType
				.getInstance();
	}

	@Override
	public void setupSdkPaths(Sdk sdk)
	{
		final SdkModificator modificator = sdk.getSdkModificator();

		DartSdkUtil.setupSdkPaths(sdk.getHomeDirectory(), modificator);

		modificator.commitChanges();
	}

	@Override
	public FileChooserDescriptor getHomeChooserDescriptor()
	{
		final FileChooserDescriptor result = super.getHomeChooserDescriptor();
		if(SystemInfo.isMac)
		{
			result.putUserData(PathChooserDialog.NATIVE_MAC_CHOOSER_SHOW_HIDDEN_FILES, Boolean.TRUE);
		}
		return result;
	}
}
