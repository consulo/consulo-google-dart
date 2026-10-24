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

import com.dslplatform.json.CompiledJson;
import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineRunConfigurationOptions;
import consulo.util.lang.StringUtil;
import consulo.util.xml.serializer.annotation.Attribute;
import org.jspecify.annotations.Nullable;

/**
 * @author VISTALL
 * @since 2026-10-24
 */
@CompiledJson
public class DartUnitRunConfigurationOptions extends DartCommandLineRunConfigurationOptions {
    private @Nullable String myScope;
    private @Nullable String myTestName;

    /**
     * The {@link DartUnitRunnerParameters.Scope} name, {@code null} for all tests of the file.
     */
    @Attribute("scope")
    public @Nullable String getScope() {
        return myScope;
    }

    public void setScope(@Nullable String scope) {
        myScope = scope;
    }

    @Attribute("testName")
    public @Nullable String getTestName() {
        return myTestName;
    }

    public void setTestName(@Nullable String testName) {
        myTestName = StringUtil.nullize(testName, true);
    }
}
