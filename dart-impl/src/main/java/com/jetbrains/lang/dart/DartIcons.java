package com.jetbrains.lang.dart;

import consulo.annotation.DeprecationInfo;
import consulo.google.dart.icon.DartIconGroup;
import consulo.ui.image.Image;

@Deprecated
@DeprecationInfo("Use DartIconGroup")
public class DartIcons {
  public static final Image Dart = DartIconGroup.dart();
  public static final Image DartTest = DartIconGroup.darttest();
}
