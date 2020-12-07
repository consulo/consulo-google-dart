package icons;

import consulo.annotation.DeprecationInfo;
import consulo.google.dart.icon.DartIconGroup;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.ui.image.Image;
import consulo.ui.image.ImageEffects;

@Deprecated
@DeprecationInfo("Use DartIconGroup")
public class DartIcons
{
	public static final Image Dart = DartIconGroup.dart();
	public static final Image DartTest = ImageEffects.layered(Dart, PlatformIconGroup.nodesJunitTestMark());
}
