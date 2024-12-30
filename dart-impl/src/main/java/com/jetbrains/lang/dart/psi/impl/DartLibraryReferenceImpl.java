package com.jetbrains.lang.dart.psi.impl;

import com.jetbrains.lang.dart.ide.index.DartLibraryIndex;
import com.jetbrains.lang.dart.psi.DartLibraryStatement;
import com.jetbrains.lang.dart.psi.DartQualifiedComponentName;
import com.jetbrains.lang.dart.psi.DartReference;
import com.jetbrains.lang.dart.util.DartClassResolveResult;
import com.jetbrains.lang.dart.util.DartElementGenerator;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.psi.*;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.IncorrectOperationException;
import consulo.util.collection.ArrayUtil;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;

import jakarta.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Fedor.Korotkov
 */
public class DartLibraryReferenceImpl extends DartExpressionImpl implements DartReference, PsiPolyVariantReference {
  public DartLibraryReferenceImpl(ASTNode node) {
    super(node);
  }

  @Override
  public PsiElement getElement() {
    return this;
  }

  @Override
  public PsiReference getReference() {
    return this;
  }

  @Override
  public TextRange getRangeInElement() {
    final TextRange textRange = getTextRange();
    return new TextRange(0, textRange.getEndOffset() - textRange.getStartOffset());
  }

  @Nonnull
  @Override
  public String getCanonicalText() {
    return getText();
  }

  @Override
  public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
    final DartQualifiedComponentName identifierNew = DartElementGenerator.createQIdentifierFromText(getProject(), newElementName);
    if (identifierNew != null) {
      getNode().replaceAllChildrenToChildrenOf(identifierNew.getNode());
    }
    return this;
  }

  @Override
  public PsiElement bindToElement(@Nonnull PsiElement element) throws IncorrectOperationException {
    return this;
  }

  @Override
  public boolean isReferenceTo(PsiElement element) {
    return resolve() == element;
  }

  @Override
  public boolean isSoft() {
    return false;
  }

  @Override
  public PsiElement resolve() {
    final ResolveResult[] resolveResults = multiResolve(true);

    return resolveResults.length == 0 ||
      resolveResults.length > 1 ||
      !resolveResults[0].isValidResult() ? null : resolveResults[0].getElement();
  }

  @Nonnull
  @Override
  public ResolveResult[] multiResolve(boolean incompleteCode) {
    return tryResolveLibraries();
  }

  @Nonnull
  @Override
  public DartClassResolveResult resolveDartClass() {
    return DartClassResolveResult.EMPTY;
  }

  private ResolveResult[] tryResolveLibraries() {
    final String libraryName = StringUtil.unquoteString(getText());
    final List<VirtualFile> virtualFiles = DartLibraryIndex.findLibraryClass(this, libraryName);
    final List<PsiElementResolveResult> result = new ArrayList<PsiElementResolveResult>();
    for (VirtualFile virtualFile : virtualFiles) {
      final PsiFile psiFile = getManager().findFile(virtualFile);
      for (PsiElement root : DartResolveUtil.findDartRoots(psiFile)) {
        DartLibraryStatement[] libs = PsiTreeUtil.getChildrenOfType(root, DartLibraryStatement.class);
        if (libs == null) {
          continue;
        }

        for (DartLibraryStatement lib : libs) {
          DartQualifiedComponentName componentName = lib.getQualifiedComponentName();
          if (componentName != null && libraryName.equals(componentName.getName())) {
            result.add(new PsiElementResolveResult(componentName));
          }
        }
      }
    }
    return result.toArray(new ResolveResult[result.size()]);
  }

  @Nonnull
  @Override
  public Object[] getVariants() {
    // handled by DartLibraryNameCompletionContributor
    return ArrayUtil.EMPTY_OBJECT_ARRAY;
  }
}
