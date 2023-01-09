package com.jetbrains.lang.dart.ide.formatter.settings;

import com.jetbrains.lang.dart.DartLanguage;
import consulo.language.codeStyle.CodeStyleSettings;
import consulo.language.codeStyle.ui.setting.TabbedLanguageCodeStylePanel;

/**
 * @author: Fedor.Korotkov
 */
public class DartCodeStyleMainPanel extends TabbedLanguageCodeStylePanel {
  protected DartCodeStyleMainPanel(CodeStyleSettings currentSettings, CodeStyleSettings settings) {
    super(DartLanguage.INSTANCE, currentSettings, settings);
  }
}
