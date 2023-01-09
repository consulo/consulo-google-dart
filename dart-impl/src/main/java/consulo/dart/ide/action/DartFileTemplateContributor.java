package consulo.dart.ide.action;

import consulo.annotation.component.ExtensionImpl;
import consulo.fileTemplate.FileTemplateContributor;
import consulo.fileTemplate.FileTemplateRegistrator;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 09/01/2023
 */
@ExtensionImpl
public class DartFileTemplateContributor implements FileTemplateContributor {
  @Override
  public void register(@Nonnull FileTemplateRegistrator registrator) {
    registrator.registerInternalTemplate("Dart Class");
    registrator.registerInternalTemplate("Dart Interface");
    registrator.registerInternalTemplate("Dart Empty File");
  }
}
