package com.jetbrains.lang.dart.ide.formatter.settings;

import javax.annotation.Nonnull;

import com.intellij.openapi.options.Configurable;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import com.jetbrains.lang.dart.DartBundle;

/**
 * @author: Fedor.Korotkov
 */
public class DartCodeStyleSettingsProvider extends CodeStyleSettingsProvider {
  @Override
  public String getConfigurableDisplayName() {
    return DartBundle.message("dart.title");
  }

  @Nonnull
  @Override
  public Configurable createSettingsPage(CodeStyleSettings settings, CodeStyleSettings originalSettings) {
    return new DartCodeStyleConfigurable(settings, originalSettings);
  }
}
