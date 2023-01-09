package com.jetbrains.lang.dart.ide;

import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.psi.DartComponentName;
import consulo.annotation.component.ExtensionImpl;
import consulo.document.util.TextRange;
import consulo.language.Language;
import consulo.language.editor.ImplementationTextSelectioner;
import consulo.language.psi.PsiElement;

import javax.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class DartImplementationTextSelectioner implements ImplementationTextSelectioner {
  @Override
  public int getTextStartOffset(@Nonnull PsiElement element) {
    if (element instanceof DartComponentName) {
      element = element.getParent();
    }
    final TextRange textRange = element.getTextRange();
    return textRange.getStartOffset();
  }

  @Override
  public int getTextEndOffset(@Nonnull PsiElement element) {
    if (element instanceof DartComponentName) {
      element = element.getParent();
    }
    final TextRange textRange = element.getTextRange();
    return textRange.getEndOffset();
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return DartLanguage.INSTANCE;
  }
}
