package com.jetbrains.lang.dart;

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.jetbrains.lang.dart.lexer.DartLexer;
import com.jetbrains.lang.dart.psi.DartFile;
import com.jetbrains.lang.dart.psi.impl.DartDocCommentImpl;
import com.jetbrains.lang.dart.psi.impl.DartEmbeddedContentImpl;
import consulo.lang.LanguageVersion;

public class DartParserDefinition implements ParserDefinition
{

	@NotNull
	@Override
	public Lexer createLexer( LanguageVersion languageVersion)
	{
		return new DartLexer();
	}

	@Override
	public PsiParser createParser(LanguageVersion languageVersion)
	{
		return new DartParser();
	}

	@Override
	public IFileElementType getFileNodeType()
	{
		return DartTokenTypesSets.DART_FILE;
	}

	@NotNull
	@Override
	public TokenSet getWhitespaceTokens(LanguageVersion languageVersion)
	{
		return DartTokenTypesSets.WHITE_SPACES;
	}

	@NotNull
	@Override
	public TokenSet getCommentTokens(LanguageVersion languageVersion)
	{
		return DartTokenTypesSets.COMMENTS;
	}

	@NotNull
	@Override
	public TokenSet getStringLiteralElements(LanguageVersion languageVersion)
	{
		return TokenSet.create(DartTokenTypes.RAW_SINGLE_QUOTED_STRING, DartTokenTypes.RAW_TRIPLE_QUOTED_STRING, DartTokenTypes.OPEN_QUOTE,
				DartTokenTypes.CLOSING_QUOTE, DartTokenTypes.REGULAR_STRING_PART);
	}

	@NotNull
	@Override
	public PsiElement createElement(ASTNode node)
	{
		if(node.getElementType() == DartTokenTypesSets.EMBEDDED_CONTENT)
		{
			return new DartEmbeddedContentImpl(node);
		}
		else if(node.getElementType() == DartTokenTypesSets.MULTI_LINE_DOC_COMMENT)
		{
			return new DartDocCommentImpl(node);
		}
		return DartTokenTypes.Factory.createElement(node);
	}

	@Override
	public PsiFile createFile(FileViewProvider viewProvider)
	{
		return new DartFile(viewProvider);
	}

	@Override
	public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right)
	{
		return SpaceRequirements.MAY;
	}
}
