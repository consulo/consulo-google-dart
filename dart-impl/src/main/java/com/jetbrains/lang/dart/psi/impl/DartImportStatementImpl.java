// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.lang.dart.psi.DartComponentName;
import com.jetbrains.lang.dart.psi.DartHideCombinator;
import com.jetbrains.lang.dart.psi.DartImportStatement;
import com.jetbrains.lang.dart.psi.DartMetadata;
import com.jetbrains.lang.dart.psi.DartPathOrLibraryReference;
import com.jetbrains.lang.dart.psi.DartShowCombinator;
import com.jetbrains.lang.dart.psi.DartVisitor;
import com.jetbrains.lang.dart.util.DartPsiImplUtil;

public class DartImportStatementImpl extends DartPsiCompositeElementImpl implements DartImportStatement {

  public DartImportStatementImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitImportStatement(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<DartHideCombinator> getHideCombinatorList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartHideCombinator.class);
  }

  @Override
  @NotNull
  public List<DartMetadata> getMetadataList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartMetadata.class);
  }

  @Override
  @NotNull
  public List<DartShowCombinator> getShowCombinatorList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartShowCombinator.class);
  }

  public String getUri() {
   return DartPsiImplUtil.getUri(this);
  }

  @Override
  @NotNull
  public DartPathOrLibraryReference getLibraryExpression() {
    return findNotNullChildByClass(DartPathOrLibraryReference.class);
  }

  @Override
  @Nullable
  public DartComponentName getImportPrefix() {
    return findChildByClass(DartComponentName.class);
  }

}
