/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jetbrains.lang.dart.ide.runner.base;

import consulo.disposer.Disposable;
import consulo.execution.configuration.fragment.RunConfigurationFragment;
import consulo.execution.configuration.fragment.RunConfigurationFragmentEditor;
import consulo.execution.configuration.fragment.RunConfigurationFragmentLayout;
import consulo.execution.localize.ExecutionLocalize;
import consulo.google.dart.localize.DartLocalize;
import consulo.localize.LocalizeValue;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.process.cmd.ParametersListUtil;
import consulo.project.Project;
import consulo.ui.TextBoxWithExpandAction;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.util.lang.StringUtil;

/**
 * Options of the Dart VM, shown once set.
 *
 * @author VISTALL
 * @since 2026-10-24
 */
public class DartVmOptionsFragment implements RunConfigurationFragment<DartRunConfigurationBase> {
    @Override
    public String getId() {
        return "dartVmOptions";
    }

    @Override
    public LocalizeValue getName() {
        return DartLocalize.runConfigurationFragmentVmOptions();
    }

    @Override
    public boolean isVisibleByDefault(DartRunConfigurationBase target) {
        return target.getVmOptions() != null;
    }

    @Override
    public void applyHidden(DartRunConfigurationBase target) {
        target.setVmOptions(null);
    }

    @RequiredUIAccess
    @Override
    public RunConfigurationFragmentEditor<DartRunConfigurationBase> createEditor(Project project, Disposable parent) {
        TextBoxWithExpandAction vmOptions = TextBoxWithExpandAction.create(
            PlatformIconGroup.actionsShow(),
            DartLocalize.configVmoptionsCaption().get(),
            ParametersListUtil.DEFAULT_LINE_PARSER,
            ParametersListUtil.DEFAULT_LINE_JOINER
        );

        return new RunConfigurationFragmentEditor<>() {
            @RequiredUIAccess
            @Override
            public void addRows(RunConfigurationFragmentLayout layout) {
                layout.addLabeled(ExecutionLocalize.runConfigurationJavaVmParametersLabel(), vmOptions);
            }

            @RequiredUIAccess
            @Override
            public void reset(DartRunConfigurationBase target) {
                vmOptions.setValue(StringUtil.notNullize(target.getVmOptions()));
            }

            @RequiredUIAccess
            @Override
            public void apply(DartRunConfigurationBase target) {
                target.setVmOptions(vmOptions.getValue());
            }
        };
    }
}
