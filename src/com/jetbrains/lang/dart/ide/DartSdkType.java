package com.jetbrains.lang.dart.ide;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.PathChooserDialog;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.util.SystemInfo;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.util.DartSdkUtil;

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
	public Icon getIcon()
	{
		return icons.DartIcons.Dart;
	}

	@Nullable
	@Override
	public Icon getGroupIcon()
	{
		return getIcon();
	}

	@NotNull
	@Override
	public String getPresentableName()
	{
		return DartBundle.message("dart.sdk.name.presentable");
	}

	@Override
	public String suggestSdkName(String currentSdkName, String sdkHome)
	{
		return DartBundle.message("dart.sdk.name.suggest", getVersionString(sdkHome));
	}

	@Override
	public String getVersionString(String sdkHome)
	{
		return DartSdkUtil.getSdkVersion(sdkHome);
	}

	@Override
	public String suggestHomePath()
	{
		return null;
	}

	@Override
	public boolean isValidSdkHome(String path)
	{
		return DartSdkUtil.getCompilerPathByFolderPath(path) != null;
	}

	@Override
	public boolean isRootTypeApplicable(OrderRootType type)
	{
		return type == OrderRootType.BINARIES || type == OrderRootType.SOURCES || type == OrderRootType.DOCUMENTATION;
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
