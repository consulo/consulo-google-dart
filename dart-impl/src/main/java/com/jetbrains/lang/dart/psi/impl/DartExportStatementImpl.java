// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import java.util.List;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.lang.dart.psi.DartExportStatement;
import com.jetbrains.lang.dart.psi.DartHideCombinator;
import com.jetbrains.lang.dart.psi.DartMetadata;
import com.jetbrains.lang.dart.psi.DartPathOrLibraryReference;
import com.jetbrains.lang.dart.psi.DartShowCombinator;
import com.jetbrains.lang.dart.psi.DartVisitor;
import com.jetbrains.lang.dart.util.DartPsiImplUtil;

public class DartExportStatementImpl extends DartPsiCompositeElementImpl implements DartExportStatement {

  public DartExportStatementImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitExportStatement(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public List<DartHideCombinator> getHideCombinatorList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartHideCombinator.class);
  }

  @Override
  @Nonnull
  public List<DartMetadata> getMetadataList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartMetadata.class);
  }

  @Override
  @Nonnull
  public List<DartShowCombinator> getShowCombinatorList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartShowCombinator.class);
  }

  public String getUri() {
    return DartPsiImplUtil.getUri(this);
  }

  @Override
  @Nonnull
  public DartPathOrLibraryReference getLibraryExpression() {
    return findNotNullChildByClass(DartPathOrLibraryReference.class);
  }

}
