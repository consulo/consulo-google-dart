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

import java.util.Set;

/**
 * @author Fedor.Korotkov
 */
@ExtensionImpl
public class DartSdkType extends SdkType {
    private static final Set<String> ourAllowedRootTypeIds = Set.of(
        BinariesOrderRootType.ID,
        SourcesOrderRootType.ID,
        DocumentationOrderRootType.ID
    );

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
    public boolean isRootTypeApplicable(String type) {
        return ourAllowedRootTypeIds.contains(type);
    }

    @Override
    public void setupSdkPaths(Sdk sdk) {
        final SdkModificator modificator = sdk.getSdkModificator();

        DartSdkUtil.setupSdkPaths(sdk.getHomeDirectory(), modificator);

        modificator.commitChanges();
    }
}
