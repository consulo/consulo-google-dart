package com.jetbrains.lang.dart.highlight;

import com.jetbrains.lang.dart.DartFileType;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.wolfAnalyzer.WolfFileProblemFilter;
import consulo.virtualFileSystem.VirtualFile;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class DartProblemFileHighlightFilter implements WolfFileProblemFilter {
  public boolean isToBeHighlighted(VirtualFile virtualFile) {
    return virtualFile.getFileType() == DartFileType.INSTANCE;
  }
}
