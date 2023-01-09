package com.jetbrains.lang.dart.ide.runner.base;

import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.util.DartElementGenerator;
import consulo.execution.debug.evaluation.XDebuggerEditorsProviderBase;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.project.Project;
import consulo.virtualFileSystem.fileType.FileType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DartDebuggerEditorsProvider extends XDebuggerEditorsProviderBase {
  @Override
  @Nonnull
  public FileType getFileType() {
    return DartFileType.INSTANCE;
  }

  @Override
  protected PsiFile createExpressionCodeFragment(@Nonnull Project project,
                                                 @Nonnull String text,
                                                 @Nullable PsiElement context,
                                                 boolean isPhysical) {
    return DartElementGenerator.createExpressionCodeFragment(project, text, context, true);
  }
}