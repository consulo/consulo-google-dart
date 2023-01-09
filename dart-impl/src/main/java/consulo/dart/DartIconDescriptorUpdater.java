package consulo.dart;

import com.jetbrains.lang.dart.DartComponentType;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.icon.IconDescriptor;
import consulo.language.icon.IconDescriptorUpdater;
import consulo.language.psi.PsiElement;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 12.09.13.
 */
@ExtensionImpl
public class DartIconDescriptorUpdater implements IconDescriptorUpdater {
  @RequiredReadAction
  @Override
  public void updateIcon(@Nonnull IconDescriptor iconDescriptor, @Nonnull PsiElement element, int flags) {
    DartComponentType dartComponentType = DartComponentType.typeOf(element);
    if (dartComponentType != null) {
      iconDescriptor.setMainIcon(dartComponentType.getIcon());
    }
  }
}
