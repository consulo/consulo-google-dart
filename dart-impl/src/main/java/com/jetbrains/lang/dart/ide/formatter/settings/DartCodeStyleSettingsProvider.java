package com.jetbrains.lang.dart.ide.formatter.settings;

import com.jetbrains.lang.dart.DartLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.configurable.Configurable;
import consulo.language.Language;
import consulo.language.codeStyle.CodeStyleSettings;
import consulo.language.codeStyle.setting.CodeStyleSettingsProvider;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author Fedor.Korotkov
 */
@ExtensionImpl
public class DartCodeStyleSettingsProvider extends CodeStyleSettingsProvider {
    @Nullable
    @Override
    public Language getLanguage() {
        return DartLanguage.INSTANCE;
    }

    @Nonnull
    @Override
    public Configurable createSettingsPage(CodeStyleSettings settings, CodeStyleSettings originalSettings) {
        return new DartCodeStyleConfigurable(settings, originalSettings);
    }
}
