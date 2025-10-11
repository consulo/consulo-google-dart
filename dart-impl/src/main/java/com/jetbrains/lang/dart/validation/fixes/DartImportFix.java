package com.jetbrains.lang.dart.validation.fixes;

import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.ide.index.DartComponentInfo;
import com.jetbrains.lang.dart.util.DartImportUtil;
import consulo.codeEditor.Editor;
import consulo.google.dart.localize.DartLocalize;
import consulo.language.psi.PsiElement;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Created by fedorkorotkov.
 */
public class DartImportFix extends BaseCreateFix {
  private final String myComponentName;
  private final DartComponentInfo myInfo;

  public DartImportFix(String name, DartComponentInfo info) {
    myComponentName = name;
    myInfo = info;
  }


  @Override
  protected void applyFix(Project project, @Nonnull PsiElement psiElement, @Nullable Editor editor) {
    final String libraryId = myInfo.getLibraryId();
    if (libraryId != null) {
      DartImportUtil.insertImport(psiElement.getContainingFile(), myComponentName, libraryId);
    }
  }

  @Nonnull
  @Override
  public LocalizeValue getName() {
    final DartComponentType type = myInfo.getType();
    assert type != null;
    return DartLocalize.dartImportFixName(type.toString().toLowerCase(), myComponentName, myInfo.getLibraryId());
  }
}
