package com.jetbrains.lang.dart.ide.actions;

import consulo.google.dart.localize.DartLocalize;
import consulo.localize.LocalizeValue;

public class DartPubUpgradeAction extends DartPubActionBase {
  @Override
  protected LocalizeValue getPresentableText() {
    return DartLocalize.dartPubUpgrade();
  }

  @Override
  protected String getPubCommand() {
    return "upgrade";
  }

  @Override
  protected LocalizeValue getSuccessMessage() {
    return DartLocalize.dartPubUpgradeSuccess();
  }
}
