package com.jetbrains.lang.dart.psi;

import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.psi.impl.DartPsiCompositeElementImpl;
import consulo.language.ast.IElementType;
import consulo.language.file.FileViewProvider;
import consulo.language.impl.psi.PsiFileBase;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import consulo.virtualFileSystem.fileType.FileType;

import jakarta.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
public class DartFile extends PsiFileBase implements DartExecutionScope {
  public DartFile(@Nonnull FileViewProvider viewProvider) {
    super(viewProvider, DartLanguage.INSTANCE);
  }

  @Nonnull
  @Override
  public FileType getFileType() {
    return DartFileType.INSTANCE;
  }

  @Override
  public String toString() {
    return "Dart File";
  }

  @Override
  public boolean processDeclarations(@Nonnull PsiScopeProcessor processor,
                                     @Nonnull ResolveState state,
                                     PsiElement lastParent,
                                     @Nonnull PsiElement place) {
    return DartPsiCompositeElementImpl.processDeclarationsImpl(this, processor, state, lastParent)
      && super.processDeclarations(processor, state, lastParent, place);
  }

  @Override
  public IElementType getTokenType() {
    return getNode().getElementType();
  }
}
