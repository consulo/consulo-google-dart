package com.jetbrains.lang.dart.ide.template;

import com.jetbrains.lang.dart.DartLanguage;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.google.dart.localize.DartLocalize;
import consulo.language.editor.template.context.BaseTemplateContextType;
import consulo.language.editor.template.context.TemplateActionContext;
import consulo.language.psi.PsiFile;
import jakarta.annotation.Nonnull;

/**
 * @author Fedor.Korotkov
 */
@ExtensionImpl
public class DartTemplateContextType extends BaseTemplateContextType {
  public DartTemplateContextType() {
    super("DART", DartLocalize.dartLanguageId());
  }

  @Override
  @RequiredReadAction
  public boolean isInContext(@Nonnull TemplateActionContext context) {
    PsiFile file = context.getFile();
    return file.getLanguage() instanceof DartLanguage;
  }
}
