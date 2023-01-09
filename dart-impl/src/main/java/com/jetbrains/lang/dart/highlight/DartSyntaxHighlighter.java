package com.jetbrains.lang.dart.highlight;

import com.jetbrains.lang.dart.lexer.DartLexer;
import consulo.colorScheme.TextAttributesKey;
import consulo.language.ast.IElementType;
import consulo.language.editor.highlight.SyntaxHighlighterBase;
import consulo.language.lexer.Lexer;
import consulo.xml.ide.highlighter.HtmlFileHighlighter;
import consulo.xml.ide.highlighter.XmlFileHighlighter;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

import static com.jetbrains.lang.dart.DartTokenTypes.*;
import static com.jetbrains.lang.dart.DartTokenTypesSets.*;

public class DartSyntaxHighlighter extends SyntaxHighlighterBase {
  private static final Map<IElementType, TextAttributesKey> ATTRIBUTES = new HashMap<IElementType, TextAttributesKey>();

  static {
    fillMap(ATTRIBUTES, RESERVED_WORDS, DartSyntaxHighlighterColors.KEYWORD);

    fillMap(ATTRIBUTES, BINARY_OPERATORS, DartSyntaxHighlighterColors.OPERATION_SIGN);
    fillMap(ATTRIBUTES, LOGIC_OPERATORS, DartSyntaxHighlighterColors.OPERATION_SIGN);
    fillMap(ATTRIBUTES, BITWISE_OPERATORS, DartSyntaxHighlighterColors.OPERATION_SIGN);
    fillMap(ATTRIBUTES, UNARY_OPERATORS, DartSyntaxHighlighterColors.OPERATION_SIGN);

    fillMap(ATTRIBUTES, STRINGS, DartSyntaxHighlighterColors.STRING);

    ATTRIBUTES.put(HEX_NUMBER, DartSyntaxHighlighterColors.NUMBER);
    ATTRIBUTES.put(NUMBER, DartSyntaxHighlighterColors.NUMBER);


    ATTRIBUTES.put(LPAREN, DartSyntaxHighlighterColors.PARENTHS);
    ATTRIBUTES.put(RPAREN, DartSyntaxHighlighterColors.PARENTHS);

    ATTRIBUTES.put(LBRACE, DartSyntaxHighlighterColors.BRACES);
    ATTRIBUTES.put(RBRACE, DartSyntaxHighlighterColors.BRACES);
    ATTRIBUTES.put(SHORT_TEMPLATE_ENTRY_START, DartSyntaxHighlighterColors.BRACES);
    ATTRIBUTES.put(LONG_TEMPLATE_ENTRY_START, DartSyntaxHighlighterColors.BRACES);
    ATTRIBUTES.put(LONG_TEMPLATE_ENTRY_END, DartSyntaxHighlighterColors.BRACES);

    ATTRIBUTES.put(LBRACKET, DartSyntaxHighlighterColors.BRACKETS);
    ATTRIBUTES.put(RBRACKET, DartSyntaxHighlighterColors.BRACKETS);

    ATTRIBUTES.put(COMMA, DartSyntaxHighlighterColors.COMMA);
    ATTRIBUTES.put(DOT, DartSyntaxHighlighterColors.DOT);
    ATTRIBUTES.put(SEMICOLON, DartSyntaxHighlighterColors.SEMICOLON);

    ATTRIBUTES.put(SINGLE_LINE_COMMENT, DartSyntaxHighlighterColors.LINE_COMMENT);
    ATTRIBUTES.put(SINGLE_LINE_DOC_COMMENT, DartSyntaxHighlighterColors.DOC_COMMENT);
    ATTRIBUTES.put(MULTI_LINE_COMMENT, DartSyntaxHighlighterColors.BLOCK_COMMENT);
    ATTRIBUTES.put(MULTI_LINE_DOC_COMMENT, DartSyntaxHighlighterColors.DOC_COMMENT);

    ATTRIBUTES.put(BAD_CHARACTER, DartSyntaxHighlighterColors.BAD_CHARACTER);

    HtmlFileHighlighter.registerEmbeddedTokenAttributes(ATTRIBUTES, null);
    XmlFileHighlighter.registerEmbeddedTokenAttributes(ATTRIBUTES, null);
  }

  @Override
  @Nonnull
  public Lexer getHighlightingLexer() {
    return new DartLexer();
  }

  @Override
  @Nonnull
  public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
    return pack(ATTRIBUTES.get(tokenType));
  }
}
