package com.jetbrains.lang.dart;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.ast.IElementType;
import consulo.language.editor.highlight.SyntaxHighlighter;
import consulo.language.editor.highlight.SyntaxHighlighterFactory;
import consulo.language.lexer.Lexer;
import consulo.xml.lang.HtmlScriptContentProvider;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

@ExtensionImpl
public class DartScriptContentProvider implements HtmlScriptContentProvider {
  @Override
  public IElementType getScriptElementType() {
    return DartTokenTypesSets.EMBEDDED_CONTENT;
  }

  @Nullable
  @Override
  public Lexer getHighlightingLexer() {
    SyntaxHighlighter syntaxHighlighter = SyntaxHighlighterFactory.getSyntaxHighlighter(DartFileType.INSTANCE, null, null);
    return syntaxHighlighter != null ? syntaxHighlighter.getHighlightingLexer() : null;
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return DartLanguage.INSTANCE;
  }
}
