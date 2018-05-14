package com.jetbrains.lang.dart.ide.template.macro;

import javax.annotation.Nonnull;

import com.jetbrains.lang.dart.psi.DartClass;

/**
 * @author: Fedor.Korotkov
 */
public class DartIterableVariableMacro extends DartFilterByClassMacro {
  @Override
  public String getName() {
    return "dartIterableVariable";
  }

  @Override
  public String getPresentableName() {
    return "dartIterableVariable()";
  }

  @Override
  protected boolean filter(@Nonnull DartClass dartClass) {
    return dartClass.findMemberByName("iterator") != null;
  }
}
