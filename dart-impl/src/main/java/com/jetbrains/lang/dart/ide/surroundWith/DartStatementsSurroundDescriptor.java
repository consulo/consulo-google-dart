package com.jetbrains.lang.dart.ide.surroundWith;

import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.ide.surroundWith.statement.*;
import com.jetbrains.lang.dart.util.DartRefactoringUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.surroundWith.SurroundDescriptor;
import consulo.language.editor.surroundWith.Surrounder;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;

import jakarta.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class DartStatementsSurroundDescriptor implements SurroundDescriptor {
  @Nonnull
  public PsiElement[] getElementsToSurround(PsiFile file, int startOffset, int endOffset) {
    final PsiElement[] statements = DartRefactoringUtil.findStatementsInRange(file, startOffset, endOffset);
    if (statements == null) return PsiElement.EMPTY_ARRAY;
    return statements;
  }

  @Nonnull
  public Surrounder[] getSurrounders() {
    return new Surrounder[]{
      new DartWithIfSurrounder(),
      new DartWithIfElseSurrounder(),
      new DartWithWhileSurrounder(),
      new DartWithDoWhileSurrounder(),
      new DartWithForSurrounder(),
      new DartWithTryCatchSurrounder(),
      new DartWithTryCatchFinallySurrounder()
    };
  }

  @Override
  public boolean isExclusive() {
    return false;
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return DartLanguage.INSTANCE;
  }
}
