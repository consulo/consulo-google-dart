package com.jetbrains.lang.dart.validation.fixes;

import com.jetbrains.lang.dart.ide.DartWritingAccessProvider;
import consulo.codeEditor.Editor;
import consulo.codeEditor.ScrollType;
import consulo.fileEditor.FileEditor;
import consulo.fileEditor.FileEditorManager;
import consulo.fileEditor.TextEditor;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.navigation.OpenFileDescriptorFactory;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BaseCreateFix extends FixAndIntentionAction {
  @Override
  public boolean startInWriteAction() {
    return true;
  }

  protected static boolean isInDartSdkOrDartPackagesFolder(final @Nonnull PsiFile psiFile) {
    final VirtualFile vFile = psiFile.getOriginalFile().getVirtualFile();
    return vFile != null && DartWritingAccessProvider.isInDartSdkOrDartPackagesFolder(psiFile.getProject(), vFile);
  }

  @Nullable
  protected static Editor navigate(Project project, int offset, @Nullable VirtualFile vfile) {
    if (vfile == null) {
      return null;
    }
    OpenFileDescriptorFactory.getInstance(project).builder(vfile).offset(offset).build().navigate(true); // properly contributes to editing
    // history
    FileEditor fileEditor = FileEditorManager.getInstance(project).getSelectedEditor(vfile);
    if (fileEditor instanceof TextEditor) {
      final Editor editor = ((TextEditor)fileEditor).getEditor();
      editor.getCaretModel().moveToOffset(offset);
      editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
      return editor;
    }
    return null;
  }

  @Nullable
  protected PsiElement findAnchor(PsiElement element) {
    PsiElement scopeBody = getScopeBody(element);
    PsiElement result = element;
    while (result.getParent() != null && result.getParent() != scopeBody) {
      result = result.getParent();
    }
    return result;
  }

  @Nullable
  protected PsiElement getScopeBody(PsiElement element) {
    return null;
  }
}
