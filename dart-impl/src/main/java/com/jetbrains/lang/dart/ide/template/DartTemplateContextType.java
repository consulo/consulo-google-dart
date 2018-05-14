package com.jetbrains.lang.dart.ide.template;

import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.psi.PsiFile;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartLanguage;
import javax.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
public class DartTemplateContextType extends TemplateContextType {
  protected DartTemplateContextType() {
    super("DART", DartBundle.message("dart.language.id"));
  }

  @Override
  public boolean isInContext(@Nonnull PsiFile file, int offset) {
    return file.getLanguage() instanceof DartLanguage;
  }
}
