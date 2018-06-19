package com.jetbrains.lang.dart.lexer;

import static com.jetbrains.lang.dart.DartTokenTypesSets.MULTI_LINE_COMMENT_BODY;

import com.intellij.lexer.MergingLexerAdapter;
import com.intellij.psi.tree.TokenSet;

public class DartDocLexer extends MergingLexerAdapter {

  public DartDocLexer() {
    super(new _DartDocLexer(), TokenSet.create(MULTI_LINE_COMMENT_BODY));
  }
}
