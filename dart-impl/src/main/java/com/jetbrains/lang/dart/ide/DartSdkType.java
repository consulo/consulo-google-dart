package com.jetbrains.lang.dart.ide;

import com.jetbrains.lang.dart.util.DartSdkUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.Application;
import consulo.content.OrderRootType;
import consulo.content.base.BinariesOrderRootType;
import consulo.content.base.DocumentationOrderRootType;
import consulo.content.base.SourcesOrderRootType;
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkModificator;
import consulo.content.bundle.SdkType;
import consulo.google.dart.icon.DartIconGroup;
import consulo.google.dart.localize.DartLocalize;

/**
 * @author Fedor.Korotkov
 */
@ExtensionImpl
public class DartSdkType extends SdkType {
    public static DartSdkType getInstance() {
        return Application.get().getExtensionPoint(SdkType.class).findExtension(DartSdkType.class);
    }

    public DartSdkType() {
        super("Dart SDK", DartLocalize.dartTitle(), DartIconGroup.dart());
    }

    @Override
    public String getVersionString(String sdkHome) {
        return DartSdkUtil.getSdkVersion(sdkHome);
    }

    @Override
    public boolean isValidSdkHome(String path) {
        return DartSdkUtil.getCompilerPathByFolderPath(path) != null;
    }

    @Override
    public boolean isRootTypeApplicable(OrderRootType type) {
        return type == BinariesOrderRootType.getInstance() || type == SourcesOrderRootType.getInstance() || type == DocumentationOrderRootType
            .getInstance();
    }

    @Override
    public void setupSdkPaths(Sdk sdk) {
        final SdkModificator modificator = sdk.getSdkModificator();

        DartSdkUtil.setupSdkPaths(sdk.getHomeDirectory(), modificator);

        modificator.commitChanges();
    }
}
