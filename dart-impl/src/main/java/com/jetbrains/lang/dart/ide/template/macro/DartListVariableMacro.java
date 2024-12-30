package com.jetbrains.lang.dart.ide.template.macro;

import com.jetbrains.lang.dart.psi.DartClass;
import consulo.annotation.component.ExtensionImpl;

import jakarta.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class DartListVariableMacro extends DartFilterByClassMacro {
  @Override
  public String getName() {
    return "dartListVariable";
  }

  @Override
  public String getPresentableName() {
    return "dartListVariable()";
  }

  @Override
  protected boolean filter(@Nonnull DartClass dartClass) {
    return dartClass.findMemberByName("length") != null && dartClass.findOperator("[]", null) != null;
  }
}
