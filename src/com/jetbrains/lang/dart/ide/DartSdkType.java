package com.jetbrains.lang.dart.ide;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.PathChooserDialog;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.util.xmlb.XmlSerializer;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.util.DartSdkUtil;
import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author: Fedor.Korotkov
 */
public class DartSdkType extends SdkType {
  public DartSdkType() {
    super("Dart SDK");
  }

  @Override
  public Icon getIcon() {
    return icons.DartIcons.Dart_16;
  }

  @Nullable
  @Override
  public Icon getGroupIcon() {
    return getIcon(); //TODO [VISTALL]  new icon
  }

  public static DartSdkType getInstance() {
    return SdkType.findInstance(DartSdkType.class);
  }

  @Override
  public String getPresentableName() {
    return DartBundle.message("dart.sdk.name.presentable");
  }

  @Override
  public String suggestSdkName(String currentSdkName, String sdkHome) {
    return DartBundle.message("dart.sdk.name.suggest", getVersionString(sdkHome));
  }

  @Override
  public String getVersionString(String sdkHome) {
    final DartSdkData dartSdkData = DartSdkUtil.testDartSdk(sdkHome);
    return dartSdkData != null ? dartSdkData.getVersion() : null;
  }

  @Override
  public String suggestHomePath() {
    return null;
  }

  @Override
  public boolean isValidSdkHome(String path) {
    return DartSdkUtil.testDartSdk(path) != null;
  }

  @Override
  public AdditionalDataConfigurable createAdditionalDataConfigurable(SdkModel sdkModel, SdkModificator sdkModificator) {
    return null;
  }

  @Override
  public void setupSdkPaths(Sdk sdk) {
    final SdkModificator modificator = sdk.getSdkModificator();

    SdkAdditionalData data = sdk.getSdkAdditionalData();
    if (data == null) {
      data = DartSdkUtil.testDartSdk(sdk.getHomePath());
      modificator.setSdkAdditionalData(data);
    }

    DartSdkUtil.setupSdkPaths(sdk.getHomeDirectory(), modificator);

    modificator.commitChanges();
    super.setupSdkPaths(sdk);
  }

  @Override
  public SdkAdditionalData loadAdditionalData(Element additional) {
    return XmlSerializer.deserialize(additional, DartSdkData.class);
  }

  @Override
  public void saveAdditionalData(SdkAdditionalData additionalData, Element additional) {
    if (additionalData instanceof DartSdkData) {
      XmlSerializer.serializeInto(additionalData, additional);
    }
  }

  @Override
  public FileChooserDescriptor getHomeChooserDescriptor() {
    final FileChooserDescriptor result = super.getHomeChooserDescriptor();
    if (SystemInfo.isMac) {
      result.putUserData(PathChooserDialog.NATIVE_MAC_CHOOSER_SHOW_HIDDEN_FILES, Boolean.TRUE);
    }
    return result;
  }
}
