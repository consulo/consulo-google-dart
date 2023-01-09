package com.jetbrains.lang.dart.ide.copyright;

import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.psi.DartFile;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.copyright.UpdateCopyrightsProvider;
import consulo.language.copyright.UpdatePsiFileCopyright;
import consulo.language.copyright.config.CopyrightFileConfig;
import consulo.language.copyright.config.CopyrightProfile;
import consulo.language.copyright.ui.TemplateCommentPanel;
import consulo.language.psi.PsiComment;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiWhiteSpace;
import consulo.project.Project;
import consulo.virtualFileSystem.fileType.FileType;

import javax.annotation.Nonnull;

/**
 * @author Fedor.Korotkov
 */
@ExtensionImpl
public class UpdateDartCopyrightsProvider extends UpdateCopyrightsProvider<CopyrightFileConfig> {
  @Nonnull
  @Override
  public FileType getFileType() {
    return DartFileType.INSTANCE;
  }

  @Nonnull
  @Override
  public UpdatePsiFileCopyright<CopyrightFileConfig> createInstance(@Nonnull PsiFile file, @Nonnull CopyrightProfile copyrightProfile) {
    return new UpdatePsiFileCopyright<CopyrightFileConfig>(file, copyrightProfile) {
      @Override
      protected boolean accept() {
        return getFile() instanceof DartFile;
      }

      @Override
      protected void scanFile() {
        PsiElement first = getFile().getFirstChild();
        PsiElement last = first;
        PsiElement next = first;
        while (next != null) {
          if (next instanceof PsiComment || next instanceof PsiWhiteSpace) {
            next = getNextSibling(next);
          }
          else {
            break;
          }
          last = next;
        }

        if (first != null) {
          checkComments(first, last, true);
        }
        else {
          checkComments(null, null, true);
        }
      }
    };
  }

  @Nonnull
  @Override
  public CopyrightFileConfig createDefaultOptions() {
    return new CopyrightFileConfig();
  }

  @Nonnull
  @Override
  public TemplateCommentPanel createConfigurable(@Nonnull Project project,
                                                 @Nonnull TemplateCommentPanel parentPane,
                                                 @Nonnull FileType fileType) {
    return new TemplateCommentPanel(fileType, parentPane, project);
  }
}

