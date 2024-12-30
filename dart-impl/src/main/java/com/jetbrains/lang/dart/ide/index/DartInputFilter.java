package com.jetbrains.lang.dart.ide.index;

import com.jetbrains.lang.dart.DartFileType;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.fileType.FileType;
import consulo.xml.ide.highlighter.HtmlFileType;

import jakarta.annotation.Nonnull;

public class DartInputFilter implements FileBasedIndex.InputFilter {
  public static DartInputFilter INSTANCE = new DartInputFilter();

  @Override
  public boolean acceptInput(Project project, @Nonnull VirtualFile file) {
    FileType type = file.getFileType();
    return type == DartFileType.INSTANCE || type == HtmlFileType.INSTANCE;
  }
}
