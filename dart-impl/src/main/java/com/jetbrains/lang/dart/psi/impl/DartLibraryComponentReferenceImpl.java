package com.jetbrains.lang.dart.psi.impl;

import com.jetbrains.lang.dart.psi.DartId;
import com.jetbrains.lang.dart.psi.DartPathOrLibraryReference;
import com.jetbrains.lang.dart.psi.DartReference;
import com.jetbrains.lang.dart.resolve.DartResolver;
import com.jetbrains.lang.dart.util.DartClassResolveResult;
import com.jetbrains.lang.dart.util.DartElementGenerator;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiPolyVariantReference;
import consulo.language.psi.PsiReference;
import consulo.language.psi.ResolveResult;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.IncorrectOperationException;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class DartLibraryComponentReferenceImpl extends DartExpressionImpl implements DartReference, PsiPolyVariantReference {
  public DartLibraryComponentReferenceImpl(ASTNode node) {
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
    final DartId identifier = PsiTreeUtil.getChildOfType(this, DartId.class);
    final DartId identifierNew = DartElementGenerator.createIdentifierFromText(getProject(), newElementName);
    if (identifier != null && identifierNew != null) {
      getNode().replaceChild(identifier.getNode(), identifierNew.getNode());
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
    final PsiElement library = resolveLibrary();
    if (library != null) {
      return DartResolveUtil.toCandidateInfoArray(DartResolver.resolveSimpleReference(library.getContainingFile(), getText()));
    }
    return ResolveResult.EMPTY_ARRAY;
  }

  @Nonnull
  @Override
  public Object[] getVariants() {
    return ResolveResult.EMPTY_ARRAY;
  }

  @Nullable
  public PsiElement resolveLibrary() {
    final DartPsiCompositeElementImpl statement = PsiTreeUtil.getParentOfType(this, DartImportStatementImpl.class,
                                                                              DartExportStatementImpl.class);
    final DartPathOrLibraryReference pathOrLibraryReference = PsiTreeUtil.getChildOfType(statement, DartPathOrLibraryReference.class);
    return pathOrLibraryReference != null ? pathOrLibraryReference.resolve() : null;
  }

  @Nonnull
  @Override
  public DartClassResolveResult resolveDartClass() {
    return DartClassResolveResult.EMPTY;
  }
}