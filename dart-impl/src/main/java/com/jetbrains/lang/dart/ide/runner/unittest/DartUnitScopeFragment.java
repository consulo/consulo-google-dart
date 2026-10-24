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
package com.jetbrains.lang.dart.ide.runner.unittest;

import com.jetbrains.lang.dart.ide.runner.unittest.DartUnitRunnerParameters.Scope;
import consulo.disposer.Disposable;
import consulo.execution.configuration.fragment.RunConfigurationFragment;
import consulo.execution.configuration.fragment.RunConfigurationFragmentEditor;
import consulo.execution.configuration.fragment.RunConfigurationFragmentLayout;
import consulo.google.dart.localize.DartLocalize;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.ui.ComboBox;
import consulo.ui.TextBox;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.util.lang.StringUtil;

/**
 * Which tests of the file a {@link DartUnitRunConfiguration} runs: all of them, a group or a single test.
 *
 * @author VISTALL
 * @since 2026-10-24
 */
class DartUnitScopeFragment implements RunConfigurationFragment<DartUnitRunConfiguration> {
    @Override
    public String getId() {
        return "dartUnitScope";
    }

    @Override
    public LocalizeValue getName() {
        return DartLocalize.runConfigurationFragmentTestKind();
    }

    @Override
    public boolean isRemovable() {
        return false;
    }

    @RequiredUIAccess
    @Override
    public RunConfigurationFragmentEditor<DartUnitRunConfiguration> createEditor(Project project, Disposable parent) {
        ComboBox<Scope> scopeCombo = ComboBox.create(Scope.values());
        scopeCombo.setTextRenderer(scope -> scope == null ? LocalizeValue.empty() : getScopeName(scope));
        TextBox testNameField = TextBox.create();
        scopeCombo.addValueListener(event -> testNameField.setEnabled(event.getValue() != Scope.ALL));

        return new RunConfigurationFragmentEditor<>() {
            @RequiredUIAccess
            @Override
            public void addRows(RunConfigurationFragmentLayout layout) {
                layout.addLabeled(DartLocalize.dartUnitScope(), scopeCombo);
                layout.addLabeled(DartLocalize.dartUnitMethodName(), testNameField);
            }

            @RequiredUIAccess
            @Override
            public void reset(DartUnitRunConfiguration target) {
                Scope scope = target.getScope();
                scopeCombo.setValue(scope);
                testNameField.setValue(scope == Scope.ALL ? "" : StringUtil.notNullize(target.getTestName()));
                testNameField.setEnabled(scope != Scope.ALL);
            }

            @RequiredUIAccess
            @Override
            public void apply(DartUnitRunConfiguration target) {
                Scope scope = scopeCombo.getValue();
                target.setScope(scope == null ? Scope.ALL : scope);
                target.setTestName(scope == null || scope == Scope.ALL ? null : testNameField.getValue());
            }
        };
    }

    private static LocalizeValue getScopeName(Scope scope) {
        return switch (scope) {
            case METHOD -> DartLocalize.dartUnitScopeMethod();
            case GROUP -> DartLocalize.dartUnitScopeGroup();
            case ALL -> DartLocalize.dartUnitScopeAll();
        };
    }
}
