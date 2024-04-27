package com.jetbrains.lang.dart.highlight;

import com.intellij.xml.highlighter.EmbeddedTokenHighlighter;
import consulo.annotation.component.ExtensionImpl;
import consulo.colorScheme.TextAttributesKey;
import consulo.language.ast.IElementType;
import consulo.util.collection.MultiMap;
import consulo.xml.lang.xml.XMLLanguage;
import jakarta.annotation.Nonnull;

import java.util.Map;

/**
 * @author VISTALL
 * @since 2024-04-27
 */
@ExtensionImpl
public class DartEmbeddedTokenHighlighter implements EmbeddedTokenHighlighter {
  @Nonnull
  @Override
  public MultiMap<IElementType, TextAttributesKey> getEmbeddedTokenAttributes(@Nonnull XMLLanguage xmlLanguage) {
    MultiMap<IElementType, TextAttributesKey> multiMap = MultiMap.create();
    for (Map.Entry<IElementType, TextAttributesKey> entry : DartSyntaxHighlighter.ATTRIBUTES.entrySet()) {
      multiMap.putValue(entry.getKey(), entry.getValue());
    }
    return multiMap;
  }
}
