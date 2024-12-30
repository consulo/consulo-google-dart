package com.jetbrains.lang.dart.ide.runner.unittest;

import com.jetbrains.lang.dart.DartIcons;
import consulo.annotation.component.ExtensionImpl;
import consulo.dart.module.extension.DartModuleExtension;
import consulo.execution.configuration.ConfigurationFactory;
import consulo.execution.configuration.ConfigurationTypeBase;
import consulo.execution.configuration.RunConfiguration;
import consulo.google.dart.localize.DartLocalize;
import consulo.module.extension.ModuleExtensionHelper;
import consulo.project.Project;

import jakarta.annotation.Nonnull;

/**
 * @author Fedor.Korotkov
 */
@ExtensionImpl
public class DartUnitRunConfigurationType extends ConfigurationTypeBase {
  public DartUnitRunConfigurationType() {
    super("DartUnitRunConfigurationType",
          DartLocalize.runnerUnitConfigurationName(),
          DartLocalize.runnerUnitConfigurationDescription(),
          DartIcons.DartTest);
    addFactory(new DartUnitConfigurationFactory(this));
  }

  public static DartUnitRunConfigurationType getInstance() {
    return EP_NAME.findExtensionOrFail(DartUnitRunConfigurationType.class);
  }

  public static class DartUnitConfigurationFactory extends ConfigurationFactory {
    protected DartUnitConfigurationFactory(DartUnitRunConfigurationType type) {
      super(type);
    }

    @Override
    public boolean isApplicable(@Nonnull Project project) {
      return ModuleExtensionHelper.getInstance(project).hasModuleExtension(DartModuleExtension.class);
    }

    @Override
    public RunConfiguration createTemplateConfiguration(Project project) {
      return new DartUnitRunConfiguration(project, this, "Dart");
    }
  }
}
