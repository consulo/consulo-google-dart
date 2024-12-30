package com.jetbrains.lang.dart.ide.template.macro;

import com.jetbrains.lang.dart.util.DartRefactoringUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.template.Expression;
import consulo.language.editor.template.ExpressionContext;
import consulo.language.editor.template.Result;
import consulo.language.editor.template.TextResult;
import consulo.language.editor.template.macro.Macro;
import consulo.language.psi.PsiElement;

import jakarta.annotation.Nonnull;
import java.util.Set;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class DartSuggestIndexNameMacro extends Macro {
  @Override
  public String getName() {
    return "dartSuggestIndexName";
  }

  @Override
  public String getPresentableName() {
    return "dartSuggestIndexName()";
  }

  @Nonnull
  @Override
  public String getDefaultValue() {
    return "i";
  }

  @Override
  public Result calculateResult(@Nonnull Expression[] params, ExpressionContext context) {
    final PsiElement at = context.getPsiElementAtStartOffset();
    final Set<String> names = DartRefactoringUtil.collectUsedNames(at);
    for (char i = 'i'; i < 'z'; ++i) {
      if (!names.contains(Character.toString(i))) {
        return new TextResult(Character.toString(i));
      }
    }
    return null;
  }
}
