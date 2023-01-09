package consulo.dart;

import consulo.annotation.component.ExtensionImpl;
import consulo.localize.LocalizeValue;
import consulo.project.ui.notification.NotificationGroup;
import consulo.project.ui.notification.NotificationGroupContributor;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * @author VISTALL
 * @since 09/01/2023
 */
@ExtensionImpl
public class DartNotificationGroup implements NotificationGroupContributor {
  public static final NotificationGroup DART_PUB_TOOL = NotificationGroup.balloonGroup("DartPubTool", LocalizeValue.of("Dart Pub Tool"));
  public static final NotificationGroup DART2JS = NotificationGroup.balloonGroup("Dart2JS", LocalizeValue.of("Dart to JavaScript"));

  @Override
  public void contribute(@Nonnull Consumer<NotificationGroup> consumer) {
    consumer.accept(DART_PUB_TOOL);
    consumer.accept(DART2JS);
  }
}
