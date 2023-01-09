package com.jetbrains.lang.dart.ide.folding;

import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.DartTokenTypes;
import consulo.annotation.component.ExtensionImpl;
import consulo.document.Document;
import consulo.document.util.TextRange;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.editor.folding.FoldingBuilder;
import consulo.language.editor.folding.FoldingDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fedorkorotkov.
 */
@ExtensionImpl
public class DartFoldingBuilder implements FoldingBuilder {
  @Nonnull
  @Override
  public FoldingDescriptor[] buildFoldRegions(@Nonnull ASTNode node, @Nonnull Document document) {
    List<FoldingDescriptor> list = new ArrayList<FoldingDescriptor>();
    buildFolding(node, list);
    FoldingDescriptor[] descriptors = new FoldingDescriptor[list.size()];
    return list.toArray(descriptors);
  }

  private static void buildFolding(ASTNode node, List<FoldingDescriptor> list) {
    boolean isBlock = node.getElementType() == DartTokenTypes.BLOCK || node.getElementType() == DartTokenTypes.CLASS_BODY;
    if (isBlock && !node.getTextRange().isEmpty()) {
      final TextRange range = node.getTextRange();
      list.add(new FoldingDescriptor(node, range));
    }
    for (ASTNode child : node.getChildren(null)) {
      buildFolding(child, list);
    }
  }

  @Nullable
  @Override
  public String getPlaceholderText(@Nonnull ASTNode node) {
    if (node.getElementType() == DartTokenTypes.CLASS_BODY) {
      return "...";
    }
    return "{...}";
  }

  @Override
  public boolean isCollapsedByDefault(@Nonnull ASTNode node) {
    return false;
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return DartLanguage.INSTANCE;
  }
}
