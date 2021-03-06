package com.jetbrains.lang.dart.ide.template.macro;

import com.intellij.codeInsight.template.Expression;
import com.intellij.codeInsight.template.ExpressionContext;
import com.intellij.codeInsight.template.Macro;
import com.intellij.codeInsight.template.Result;
import javax.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
public class DartSuggestVariableNameMacro extends Macro {
  @Override
  public String getName() {
    return "dartSuggestVariableName";
  }

  @Override
  public String getPresentableName() {
    return "dartSuggestVariableName()";
  }

  @Nonnull
  @Override
  public String getDefaultValue() {
    return "o";
  }

  @Override
  public Result calculateResult(@Nonnull Expression[] params, ExpressionContext context) {
    return null;
  }
}
