package com.jetbrains.lang.dart.ide.editor;

import com.jetbrains.lang.dart.psi.DartComponentName;
import com.jetbrains.lang.dart.psi.DartPsiCompositeElement;
import com.jetbrains.lang.dart.psi.DartType;
import com.jetbrains.lang.dart.util.UsefulPsiTreeUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.Editor;
import consulo.language.editor.action.TypedHandlerDelegate;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.project.Project;
import consulo.virtualFileSystem.fileType.FileType;

import jakarta.annotation.Nonnull;

@ExtensionImpl(id = "Dart")
public class DartTypeHandler extends TypedHandlerDelegate {
  private boolean myAfterTypeOrComponentName = false;
  private boolean myAfterDollar = false;


  @Override
  public Result beforeCharTyped(char c,
                                Project project,
                                Editor editor,
                                PsiFile file,
                                FileType fileType) {
    if (c == '<') {
      myAfterTypeOrComponentName = checkAfterTypeOrComponentName(file, editor.getCaretModel().getOffset());
    }
    if (c == '{') {
      myAfterDollar = checkAfterDollarInString(file, editor.getCaretModel().getOffset());
    }
    return super.beforeCharTyped(c, project, editor, file, fileType);
  }

  private static boolean checkAfterTypeOrComponentName(PsiFile file, int offset) {
    PsiElement at = file.findElementAt(offset - 1);
    PsiElement toCheck = UsefulPsiTreeUtil.getPrevSiblingSkipWhiteSpacesAndComments(at, false);
    return PsiTreeUtil.getParentOfType(toCheck, DartType.class, DartComponentName.class) != null;
  }

  private static boolean checkAfterDollarInString(PsiFile file, int offset) {
    PsiElement at = file.findElementAt(offset - 1);
    final String text = at != null ? at.getText() : "";
    return text.endsWith("$") && PsiTreeUtil.getParentOfType(at, DartPsiCompositeElement.class) != null;
  }

  @Override
  public Result charTyped(char c, Project project, Editor editor, @Nonnull PsiFile file) {
    String textToInsert = null;
    if (c == '<' && myAfterTypeOrComponentName) {
      myAfterTypeOrComponentName = false;
      textToInsert = ">";
    }
    else if (c == '{' && myAfterDollar) {
      myAfterDollar = false;
      textToInsert = "}";
    }
    if (textToInsert != null) {
      int offset = editor.getCaretModel().getOffset();
      if (offset >= 0) {
        editor.getDocument().insertString(offset, textToInsert);
        editor.getCaretModel().moveToOffset(offset);
        return Result.STOP;
      }
    }
    return super.charTyped(c, project, editor, file);
  }
}
