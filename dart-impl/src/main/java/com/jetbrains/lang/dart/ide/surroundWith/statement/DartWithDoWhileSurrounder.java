package com.jetbrains.lang.dart.ide.surroundWith.statement;

import consulo.localize.LocalizeValue;

/**
 * @author: Fedor.Korotkov
 */
public class DartWithDoWhileSurrounder extends DartLiteralAndBlockStatementSurrounderBase {
  @Override
  public LocalizeValue getTemplateDescription() {
    return LocalizeValue.localizeTODO("do / while");
  }

  @Override
  protected String getTemplateText() {
    return "do{\n} while(true);";
  }
}
