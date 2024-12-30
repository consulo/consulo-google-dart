package com.jetbrains.lang.dart.ide.formatter;

import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.psi.DartFile;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.codeStyle.CodeStyleSettings;
import consulo.language.codeStyle.FormattingContext;
import consulo.language.codeStyle.FormattingModel;
import consulo.language.codeStyle.FormattingModelBuilder;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;

import jakarta.annotation.Nonnull;

/**
 * @author fedor.korotkov
 */
@ExtensionImpl
public class DartFormattingModelBuilder implements FormattingModelBuilder {
  @Nonnull
  @Override
  public FormattingModel createModel(@Nonnull FormattingContext context) {
    PsiElement element = context.getPsiElement();
    CodeStyleSettings settings = context.getCodeStyleSettings();
    final PsiFile psiFile = context.getContainingFile();
    return new DartFormattingModel(
      psiFile,
      settings,
      new DartBlock(psiFile instanceof DartFile ? psiFile.getNode() : element.getNode(), null, null, settings)
    );
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return DartLanguage.INSTANCE;
  }
}
