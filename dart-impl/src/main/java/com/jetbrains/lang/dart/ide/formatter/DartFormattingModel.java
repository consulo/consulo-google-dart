package com.jetbrains.lang.dart.ide.formatter;

import javax.annotation.Nonnull;
import com.intellij.formatting.Block;
import com.intellij.formatting.FormattingDocumentModel;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelProvider;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;

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
