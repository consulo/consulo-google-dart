package com.jetbrains.lang.dart;

import consulo.language.ast.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: Maxim.Mossienko
 * Date: 10/12/11
 * Time: 8:06 PM
 */
public class DartElementType extends IElementType {
  public DartElementType(String debug_description) {
    super(debug_description, DartLanguage.INSTANCE);
  }
}
