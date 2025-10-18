package com.jetbrains.lang.dart.ide.surroundWith.statement;

import consulo.localize.LocalizeValue;

/**
 * @author: Fedor.Korotkov
 */
public class DartWithIfSurrounder extends DartLiteralAndBlockStatementSurrounderBase {
  @Override
  public LocalizeValue getTemplateDescription() {
    return LocalizeValue.localizeTODO("if");
  }

  @Override
  protected String getTemplateText() {
    return "if(true) {\n}";
  }
}
