package com.jetbrains.lang.dart.ide.surroundWith.statement;

import consulo.localize.LocalizeValue;

/**
 * @author: Fedor.Korotkov
 */
public class DartWithWhileSurrounder extends DartLiteralAndBlockStatementSurrounderBase {
  @Override
  public LocalizeValue getTemplateDescription() {
    return LocalizeValue.localizeTODO("while");
  }

  @Override
  protected String getTemplateText() {
    return "while(true) {\n}";
  }
}
