package consulo.dart.module.extension;

import consulo.annotation.component.ExtensionImpl;
import consulo.google.dart.icon.DartIconGroup;
import consulo.localize.LocalizeValue;
import consulo.module.content.layer.ModuleExtensionProvider;
import consulo.module.content.layer.ModuleRootLayer;
import consulo.module.extension.ModuleExtension;
import consulo.module.extension.MutableModuleExtension;
import consulo.ui.image.Image;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 09/01/2023
 */
@ExtensionImpl
public class DartModuleExtensionProvider implements ModuleExtensionProvider<DartModuleExtension> {
  @Nonnull
  @Override
  public String getId() {
    return "dart";
  }

  @Nonnull
  @Override
  public LocalizeValue getName() {
    return LocalizeValue.of("Dart");
  }

  @Nonnull
  @Override
  public Image getIcon() {
    return DartIconGroup.dart();
  }

  @Nonnull
  @Override
  public ModuleExtension<DartModuleExtension> createImmutableExtension(@Nonnull ModuleRootLayer moduleRootLayer) {
    return new DartModuleExtension(getId(), moduleRootLayer);
  }

  @Nonnull
  @Override
  public MutableModuleExtension<DartModuleExtension> createMutableExtension(@Nonnull ModuleRootLayer moduleRootLayer) {
    return new DartMutableModuleExtension(getId(), moduleRootLayer);
  }
}
