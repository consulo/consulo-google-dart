package com.jetbrains.lang.dart.ide;

import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.DartTokenTypes;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.BracePair;
import consulo.language.Language;
import consulo.language.PairedBraceMatcher;
import consulo.language.ast.IElementType;
import consulo.language.psi.PsiFile;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Created by IntelliJ IDEA.
 * User: Maxim.Mossienko
 * Date: 10/12/11
 * Time: 9:07 PM
 */
@ExtensionImpl
public class DartBraceMatcher implements PairedBraceMatcher {
  private static BracePair[] ourBracePairs =
    {
      new BracePair(DartTokenTypes.LBRACE, DartTokenTypes.RBRACE, true),
      new BracePair(DartTokenTypes.LBRACKET, DartTokenTypes.RBRACKET, false),
      new BracePair(DartTokenTypes.LPAREN, DartTokenTypes.RPAREN, false)
    };

  @Override
  public BracePair[] getPairs() {
    return ourBracePairs;
  }

  @Override
  public boolean isPairedBracesAllowedBeforeType(@Nonnull IElementType lbraceType, @Nullable IElementType contextType) {
    return true;
  }

  @Override
  public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
    return openingBraceOffset;
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return DartLanguage.INSTANCE;
  }
}
