package com.jetbrains.lang.dart.ide.runner.server;

import javax.annotation.Nonnull;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.project.Project;
import com.jetbrains.lang.dart.DartBundle;
import consulo.dart.module.extension.DartModuleExtension;
import consulo.module.extension.ModuleExtensionHelper;

/**
 * @author: Fedor.Korotkov
 */
public class DartCommandLineRunConfigurationType extends ConfigurationTypeBase
{
	public static DartCommandLineRunConfigurationType getInstance()
	{
		return Extensions.findExtension(CONFIGURATION_TYPE_EP, DartCommandLineRunConfigurationType.class);
	}

	public DartCommandLineRunConfigurationType()
	{
		super("DartCommandLineRunConfigurationType", DartBundle.message("runner.command.line.configuration.name"), DartBundle.message("runner.command.line.configuration.name"), icons.DartIcons.Dart);
		addFactory(new ConfigurationFactory(this)
		{
			@Override
			public boolean isApplicable(@Nonnull Project project)
			{
				return ModuleExtensionHelper.getInstance(project).hasModuleExtension(DartModuleExtension.class);
			}

			@Override
			public RunConfiguration createTemplateConfiguration(Project project)
			{
				return new DartCommandLineRunConfiguration("Dart", project, DartCommandLineRunConfigurationType.this);
			}
		});
	}
}
