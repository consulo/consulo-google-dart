package com.jetbrains.lang.dart.ide.template;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.template.context.TemplateContextType;
import consulo.language.psi.PsiFile;

import javax.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class DartTemplateContextType extends TemplateContextType {
  public DartTemplateContextType() {
    super("DART", DartBundle.message("dart.language.id"));
  }

  @Override
  public boolean isInContext(@Nonnull PsiFile file, int offset) {
    return file.getLanguage() instanceof DartLanguage;
  }
}
