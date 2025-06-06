package com.jetbrains.lang.dart.ide.surroundWith.statement;

import com.jetbrains.lang.dart.psi.DartTryStatement;
import consulo.language.psi.PsiElement;

import jakarta.annotation.Nullable;

public class DartWithTryCatchSurrounder extends DartBlockStatementSurrounderBase {
  @Override
  public String getTemplateDescription() {
    return "try / catch";
  }

  @Override
  protected String getTemplateText() {
    return "try {\n} catch (e) {\ncaret_here: print(e);\n}";
  }

  @Override
  @Nullable
  protected PsiElement findElementToDelete(PsiElement surrounder) {
    //noinspection ConstantConditions
    return ((DartTryStatement)surrounder).getOnPartList().get(0).getBlock().getStatements().getFirstChild(); // todo preselect print(e);
  }
}
