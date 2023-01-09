package consulo.dart;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.template.DefaultLiveTemplatesProvider;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 09/01/2023
 */
@ExtensionImpl
public class DartLiveTemplateProvider implements DefaultLiveTemplatesProvider {
  @Nonnull
  @Override
  public String[] getDefaultLiveTemplateFiles() {
    return new String[]{"liveTemplates/dart_miscellaneous.xml", "liveTemplates/dart_iterations.xml", "liveTemplates/dart_surround.xml"};
  }
}
