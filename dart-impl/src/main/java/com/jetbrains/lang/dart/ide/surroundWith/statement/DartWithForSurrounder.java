package com.jetbrains.lang.dart.ide.surroundWith.statement;

import com.jetbrains.lang.dart.psi.DartForLoopPartsInBraces;
import consulo.language.psi.PsiElement;

import javax.annotation.Nullable;

/**
 * @author: Fedor.Korotkov
 */
public class DartWithForSurrounder extends DartBlockAndChildStatementSurrounderBase<DartForLoopPartsInBraces> {
  @Override
  public String getTemplateDescription() {
    return "for";
  }

  @Override
  protected String getTemplateText() {
    return "for(a in []){\n}";
  }

  @Override
  protected Class<DartForLoopPartsInBraces> getClassToDelete() {
    return DartForLoopPartsInBraces.class;
  }

  @Nullable
  @Override
  protected PsiElement findElementToDelete(PsiElement surrounder) {
    PsiElement result = super.findElementToDelete(surrounder);
    return result instanceof DartForLoopPartsInBraces ? ((DartForLoopPartsInBraces)result).getForLoopParts() : null;
  }
}
