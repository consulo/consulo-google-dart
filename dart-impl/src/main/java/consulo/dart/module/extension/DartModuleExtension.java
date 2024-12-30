package consulo.dart.module.extension;

import com.jetbrains.lang.dart.ide.DartSdkType;
import consulo.content.bundle.SdkType;
import consulo.module.content.layer.ModuleRootLayer;
import consulo.module.content.layer.extension.ModuleExtensionWithSdkBase;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 17:49/08.06.13
 */
public class DartModuleExtension extends ModuleExtensionWithSdkBase<DartModuleExtension> {
  public DartModuleExtension(@Nonnull String id, @Nonnull ModuleRootLayer module) {
    super(id, module);
  }

  @Nonnull
  @Override
  public Class<? extends SdkType> getSdkTypeClass() {
    return DartSdkType.class;
  }
}
