package icons;

import consulo.annotation.DeprecationInfo;
import consulo.google.dart.icon.DartIconGroup;
import consulo.ui.image.Image;

@Deprecated
@DeprecationInfo("Use DartIconGroup")
public class DartIcons {
  public static final Image DartLang = DartIconGroup.dartLang();
  public static final Image Dart = DartIconGroup.dart();
  public static final Image DartTest = DartIconGroup.dart_test();
}
