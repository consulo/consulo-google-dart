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
package com.jetbrains.lang.dart.ide.runner.server;

import consulo.util.xml.serializer.annotation.MapAnnotation;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The bean properties a Dart command line configuration wrote before the fragment model.
 *
 * @author VISTALL
 * @since 2026-10-24
 */
public class DartCommandLineRunConfigurationLegacyState {
    private @Nullable String myFilePath;
    private @Nullable String myVMOptions;
    private @Nullable String myArguments;
    private @Nullable String myWorkingDirectory;
    private Map<String, String> myEnvs = new LinkedHashMap<>();
    private boolean myIncludeParentEnvs = true;

    public @Nullable String getFilePath() {
        return myFilePath;
    }

    public void setFilePath(@Nullable String filePath) {
        myFilePath = filePath;
    }

    public @Nullable String getVMOptions() {
        return myVMOptions;
    }

    public void setVMOptions(@Nullable String vmOptions) {
        myVMOptions = vmOptions;
    }

    public @Nullable String getArguments() {
        return myArguments;
    }

    public void setArguments(@Nullable String arguments) {
        myArguments = arguments;
    }

    public @Nullable String getWorkingDirectory() {
        return myWorkingDirectory;
    }

    public void setWorkingDirectory(@Nullable String workingDirectory) {
        myWorkingDirectory = workingDirectory;
    }

    @MapAnnotation(surroundWithTag = false, surroundKeyWithTag = false, surroundValueWithTag = false)
    public Map<String, String> getEnvs() {
        return myEnvs;
    }

    public void setEnvs(@Nullable Map<String, String> envs) {
        if (envs != null) {
            myEnvs = envs;
        }
    }

    public boolean isIncludeParentEnvs() {
        return myIncludeParentEnvs;
    }

    public void setIncludeParentEnvs(boolean includeParentEnvs) {
        myIncludeParentEnvs = includeParentEnvs;
    }
}
