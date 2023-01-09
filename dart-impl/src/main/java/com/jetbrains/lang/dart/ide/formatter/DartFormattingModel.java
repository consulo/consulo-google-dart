package com.jetbrains.lang.dart.ide.formatter;

import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.codeStyle.*;
import consulo.language.psi.PsiFile;

import javax.annotation.Nonnull;

public class DartFormattingModel implements FormattingModel {
  private final FormattingModel myModel;

  public DartFormattingModel(final PsiFile file,
                             CodeStyleSettings settings,
                             final Block rootBlock) {
    myModel = FormattingModelProvider.createFormattingModelForPsiFile(file, rootBlock, settings);
  }

  @Nonnull
  public Block getRootBlock() {
    return myModel.getRootBlock();
  }

  @Nonnull
  public FormattingDocumentModel getDocumentModel() {
    return myModel.getDocumentModel();
  }

  public TextRange replaceWhiteSpace(TextRange textRange, String whiteSpace) {
    return myModel.replaceWhiteSpace(textRange, whiteSpace);
  }

  public TextRange shiftIndentInsideRange(ASTNode node, TextRange range, int indent) {
    return myModel.shiftIndentInsideRange(node, range, indent);
  }

  public void commitChanges() {
    myModel.commitChanges();
  }
}
