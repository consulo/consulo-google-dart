package com.jetbrains.lang.dart.ide.template.macro;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.template.Expression;
import consulo.language.editor.template.ExpressionContext;
import consulo.language.editor.template.Result;
import consulo.language.editor.template.macro.Macro;

import jakarta.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
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
