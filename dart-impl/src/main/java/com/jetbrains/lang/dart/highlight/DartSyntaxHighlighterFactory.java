package com.jetbrains.lang.dart.highlight;

import javax.annotation.Nonnull;

import com.intellij.openapi.fileTypes.SingleLazyInstanceSyntaxHighlighterFactory;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;

/**
 * Created by IntelliJ IDEA.
 * User: Maxim.Mossienko
 * Date: 10/12/11
 * Time: 9:01 PM
 */
public class DartSyntaxHighlighterFactory extends SingleLazyInstanceSyntaxHighlighterFactory
{
	@Nonnull
	@Override
	protected SyntaxHighlighter createHighlighter()
	{
		return new DartSyntaxHighlighter();
	}
}
