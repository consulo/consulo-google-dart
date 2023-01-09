package com.jetbrains.lang.dart.util;

import java.io.File;
import java.io.IOException;

import consulo.virtualFileSystem.LocalFileSystem;
import org.junit.Assert;
import consulo.ide.impl.idea.openapi.util.io.FileUtil;
import consulo.virtualFileSystem.VirtualFile;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;

public class DartSdkTestUtil {
  public static void configFakeSdk(CodeInsightTestFixture fixture) {
    String sdkHome = DartTestUtils.BASE_TEST_DATA_PATH +  FileUtil.toSystemDependentName("/sdk/");

    final File targetFile = new File(fixture.getTempDirPath() + "/dart-sdk");
    try {
      FileUtil.copyDir(new File(sdkHome), targetFile);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }

    final VirtualFile file = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(targetFile);
    Assert.assertNotNull(file);
    file.refresh(false, true);

    //final PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
    //propertiesComponent.setValue(DartSettingsUtil.DART_SDK_PATH, file.getPath());
  }
}
