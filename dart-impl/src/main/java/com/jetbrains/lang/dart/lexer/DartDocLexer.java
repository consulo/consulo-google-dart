package com.jetbrains.lang.dart.lexer;

import consulo.language.ast.TokenSet;
import consulo.language.lexer.MergingLexerAdapter;

import static com.jetbrains.lang.dart.DartTokenTypesSets.MULTI_LINE_COMMENT_BODY;

public class DartDocLexer extends MergingLexerAdapter {

  public DartDocLexer() {
    super(new _DartDocLexer(), TokenSet.create(MULTI_LINE_COMMENT_BODY));
  }
}
