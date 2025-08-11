package com.jetbrains.lang.dart.ide.formatter.settings;

import consulo.google.dart.localize.DartLocalize;
import consulo.language.codeStyle.CodeStyleSettings;
import consulo.language.codeStyle.ui.setting.CodeStyleAbstractConfigurable;
import consulo.language.codeStyle.ui.setting.CodeStyleAbstractPanel;

import jakarta.annotation.Nonnull;

/**
 * @author Fedor.Korotkov
 */
public class DartCodeStyleConfigurable extends CodeStyleAbstractConfigurable {
  public DartCodeStyleConfigurable(@Nonnull CodeStyleSettings settings, CodeStyleSettings cloneSettings) {
    super(settings, cloneSettings, DartLocalize.dartTitle());
  }

  @Override
  protected CodeStyleAbstractPanel createPanel(CodeStyleSettings settings) {
    return new DartCodeStyleMainPanel(getCurrentSettings(), settings);
  }

  @Override
  public String getHelpTopic() {
    return "reference.settingsdialog.codestyle.dart";
  }
}
