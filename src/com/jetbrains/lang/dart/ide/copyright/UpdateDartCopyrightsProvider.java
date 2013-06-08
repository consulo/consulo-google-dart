package com.jetbrains.lang.dart.ide.copyright;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.jetbrains.lang.dart.psi.DartFile;
import com.maddyhome.idea.copyright.CopyrightProfile;
import com.maddyhome.idea.copyright.psi.UpdateCopyright;
import com.maddyhome.idea.copyright.psi.UpdateCopyrightsProvider;
import com.maddyhome.idea.copyright.psi.UpdatePsiFileCopyright;

/**
 * @author: Fedor.Korotkov
 */
public class UpdateDartCopyrightsProvider extends UpdateCopyrightsProvider {

  public UpdateCopyright createInstance(final Project project,
                                        final Module module,
                                        final VirtualFile file,
                                        final FileType base,
                                        final CopyrightProfile options) {
    return new UpdateDartFileCopyright(project, module, file, options);
  }

  private static class UpdateDartFileCopyright extends UpdatePsiFileCopyright {
    public UpdateDartFileCopyright(final Project project,
                                   final Module module,
                                   final VirtualFile file,
                                   final CopyrightProfile options) {
      super(project, module, file, options);
    }

    @Override
    protected void scanFile()
    {
      PsiElement first = getFile().getFirstChild();
      PsiElement last = first;
      PsiElement next = first;
      while (next != null)
      {
        if (next instanceof PsiComment || next instanceof PsiWhiteSpace)
        {
          next = getNextSibling(next);
        }
        else
        {
          break;
        }
        last = next;
      }

      if (first != null)
      {
        checkComments(first, last, true);
      }
      else
      {
        checkComments(null, null, true);
      }
    }

    protected boolean accept() {
      return getFile() instanceof DartFile;
    }
  }
}

