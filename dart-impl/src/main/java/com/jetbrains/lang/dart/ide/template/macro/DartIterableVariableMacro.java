package com.jetbrains.lang.dart.ide.template.macro;

import com.jetbrains.lang.dart.psi.DartClass;
import consulo.annotation.component.ExtensionImpl;

import jakarta.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
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
