package com.jetbrains.lang.dart.ide.runner.server;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartFileType;
import consulo.content.bundle.Sdk;
import consulo.dart.module.extension.DartModuleExtension;
import consulo.execution.configuration.RuntimeConfigurationError;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.project.Project;
import consulo.util.io.FileUtil;
import consulo.util.lang.StringUtil;
import consulo.util.xml.serializer.annotation.MapAnnotation;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public class DartCommandLineRunnerParameters implements Cloneable {
  private
  @Nullable
  String myFilePath = null;
  private
  @Nullable
  String myVMOptions = null;
  private
  @Nullable
  String myArguments = null;
  private
  @Nullable
  String myWorkingDirectory = null;
  private
  @Nonnull
  Map<String, String> myEnvs = new LinkedHashMap<String, String>();
  private boolean myIncludeParentEnvs = true;

  @Nullable
  public String getFilePath() {
    return myFilePath;
  }

  public void setFilePath(final @Nullable String filePath) {
    myFilePath = filePath;
  }

  @Nullable
  public String getVMOptions() {
    return myVMOptions;
  }

  public void setVMOptions(final @Nullable String vmOptions) {
    myVMOptions = vmOptions;
  }

  @Nullable
  public String getArguments() {
    return myArguments;
  }

  public void setArguments(final @Nullable String arguments) {
    myArguments = arguments;
  }

  @Nullable
  public String getWorkingDirectory() {
    return myWorkingDirectory;
  }

  public void setWorkingDirectory(final @Nullable String workingDirectory) {
    myWorkingDirectory = workingDirectory;
  }

  @Nonnull
  @MapAnnotation(surroundWithTag = false, surroundKeyWithTag = false, surroundValueWithTag = false)
  public Map<String, String> getEnvs() {
    return myEnvs;
  }

  public void setEnvs(@SuppressWarnings("NullableProblems") final Map<String, String> envs) {
    if (envs != null) { // null comes from old projects or if storage corrupted
      myEnvs = envs;
    }
  }

  public boolean isIncludeParentEnvs() {
    return myIncludeParentEnvs;
  }

  public void setIncludeParentEnvs(final boolean includeParentEnvs) {
    myIncludeParentEnvs = includeParentEnvs;
  }

  @Nonnull
  public VirtualFile getDartFile() throws RuntimeConfigurationError {
    final String filePath = getFilePath();
    if (StringUtil.isEmptyOrSpaces(filePath)) {
      throw new RuntimeConfigurationError(DartBundle.message("path.to.dart.file.not.set"));
    }

    final VirtualFile dartFile = LocalFileSystem.getInstance().findFileByPath(filePath);
    if (dartFile == null || dartFile.isDirectory()) {
      throw new RuntimeConfigurationError(DartBundle.message("dart.file.not.found", FileUtil.toSystemDependentName(filePath)));
    }

    if (dartFile.getFileType() != DartFileType.INSTANCE) {
      throw new RuntimeConfigurationError(DartBundle.message("not.a.dart.file", FileUtil.toSystemDependentName(filePath)));
    }

    return dartFile;
  }

  public Sdk getSdk(Project project) throws RuntimeConfigurationError {
    VirtualFile dartFile = getDartFile();
    Module moduleForFile = ModuleUtilCore.findModuleForFile(dartFile, project);
    if (moduleForFile == null) {
      throw new RuntimeConfigurationError("No Module");
    }

    Sdk sdk = ModuleUtilCore.getSdk(moduleForFile, DartModuleExtension.class);
    if (sdk == null) {
      throw new RuntimeConfigurationError(DartBundle.message("dart.sdk.is.not.configured"));
    }
    return sdk;
  }

  public void check(final @Nonnull Project project) throws RuntimeConfigurationError {
    // check main dart file  and sdk
    getSdk(project);

    // check working directory
    final String workDirPath = getWorkingDirectory();
    if (!StringUtil.isEmptyOrSpaces(workDirPath)) {
      final VirtualFile workDir = LocalFileSystem.getInstance().findFileByPath(workDirPath);
      if (workDir == null || !workDir.isDirectory()) {
        throw new RuntimeConfigurationError(DartBundle.message("work.dir.does.not.exist", FileUtil.toSystemDependentName(workDirPath)));
      }
    }
  }

  @Override
  protected DartCommandLineRunnerParameters clone() {
    try {
      final DartCommandLineRunnerParameters clone = (DartCommandLineRunnerParameters)super.clone();
      clone.myEnvs = new LinkedHashMap<String, String>();
      clone.myEnvs.putAll(myEnvs);
      return clone;
    }
    catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }
}
