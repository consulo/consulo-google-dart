package com.jetbrains.lang.dart.ide.runner.server;

import consulo.annotation.component.ExtensionImpl;
import consulo.dart.module.extension.DartModuleExtension;
import consulo.execution.configuration.ConfigurationFactory;
import consulo.execution.configuration.ConfigurationTypeBase;
import consulo.execution.configuration.RunConfiguration;
import consulo.google.dart.icon.DartIconGroup;
import consulo.google.dart.localize.DartLocalize;
import consulo.module.extension.ModuleExtensionHelper;
import consulo.project.Project;

import javax.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class DartCommandLineRunConfigurationType extends ConfigurationTypeBase {
  public static DartCommandLineRunConfigurationType getInstance() {
    return EP_NAME.findExtensionOrFail(DartCommandLineRunConfigurationType.class);
  }

  public DartCommandLineRunConfigurationType() {
    super("DartCommandLineRunConfigurationType",
          DartLocalize.runnerCommandLineConfigurationName(),
          DartIconGroup.dart());
    addFactory(new ConfigurationFactory(this) {
      @Override
      public boolean isApplicable(@Nonnull Project project) {
        return ModuleExtensionHelper.getInstance(project).hasModuleExtension(DartModuleExtension.class);
      }

      @Override
      public RunConfiguration createTemplateConfiguration(Project project) {
        return new DartCommandLineRunConfiguration("Dart", project, DartCommandLineRunConfigurationType.this);
      }
    });
  }
}
