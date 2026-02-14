package com.jetbrains.lang.dart;

import com.jetbrains.lang.dart.lexer.DartLexer;
import com.jetbrains.lang.dart.psi.DartFile;
import com.jetbrains.lang.dart.psi.impl.DartDocCommentImpl;
import com.jetbrains.lang.dart.psi.impl.DartEmbeddedContentImpl;
import com.jetbrains.lang.dart.psi.impl.DartTokenTypesFactory;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IFileElementType;
import consulo.language.ast.TokenSet;
import consulo.language.file.FileViewProvider;
import consulo.language.lexer.Lexer;
import consulo.language.parser.ParserDefinition;
import consulo.language.parser.PsiParser;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.version.LanguageVersion;
import jakarta.annotation.Nonnull;

@ExtensionImpl
public class DartParserDefinition implements ParserDefinition {

  @Nonnull
  @Override
  public Language getLanguage() {
    return DartLanguage.INSTANCE;
  }

  @Nonnull
  @Override
  public Lexer createLexer(LanguageVersion languageVersion) {
    return new DartLexer();
  }

  @Override
  public PsiParser createParser(LanguageVersion languageVersion) {
    return new DartParser();
  }

  @Override
  public IFileElementType getFileNodeType() {
    return DartTokenTypesSets.DART_FILE;
  }

  @Nonnull
  @Override
  public TokenSet getWhitespaceTokens(LanguageVersion languageVersion) {
    return DartTokenTypesSets.WHITE_SPACES;
  }

  @Nonnull
  @Override
  public TokenSet getCommentTokens(LanguageVersion languageVersion) {
    return DartTokenTypesSets.COMMENTS;
  }

  @Nonnull
  @Override
  public TokenSet getStringLiteralElements(LanguageVersion languageVersion) {
    return TokenSet.create(DartTokenTypes.RAW_SINGLE_QUOTED_STRING, DartTokenTypes.RAW_TRIPLE_QUOTED_STRING, DartTokenTypes.OPEN_QUOTE,
                           DartTokenTypes.CLOSING_QUOTE, DartTokenTypes.REGULAR_STRING_PART);
  }

  @Nonnull
  @Override
  public PsiElement createElement(ASTNode node) {
    if (node.getElementType() == DartTokenTypesSets.EMBEDDED_CONTENT) {
      return new DartEmbeddedContentImpl(node);
    }
    else if (node.getElementType() == DartTokenTypesSets.MULTI_LINE_DOC_COMMENT) {
      return new DartDocCommentImpl(node);
    }
    return DartTokenTypesFactory.createElement(node);
  }

  @Override
  public PsiFile createFile(FileViewProvider viewProvider) {
    return new DartFile(viewProvider);
  }

  @Override
  public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
    return SpaceRequirements.MAY;
  }
}
