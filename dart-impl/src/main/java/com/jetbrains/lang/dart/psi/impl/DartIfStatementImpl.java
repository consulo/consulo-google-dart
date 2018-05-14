// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.Nonnull;

import com.jetbrains.lang.dart.psi.*;

public class DartIfStatementImpl extends DartPsiCompositeElementImpl implements DartIfStatement {

  public DartIfStatementImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitIfStatement(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public List<DartAssertStatement> getAssertStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartAssertStatement.class);
  }

  @Override
  @Nonnull
  public List<DartBlock> getBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartBlock.class);
  }

  @Override
  @Nonnull
  public List<DartBreakStatement> getBreakStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartBreakStatement.class);
  }

  @Override
  @Nonnull
  public List<DartContinueStatement> getContinueStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartContinueStatement.class);
  }

  @Override
  @Nonnull
  public List<DartDoWhileStatement> getDoWhileStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartDoWhileStatement.class);
  }

  @Override
  @Nonnull
  public List<DartExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartExpression.class);
  }

  @Override
  @Nonnull
  public List<DartForStatement> getForStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartForStatement.class);
  }

  @Override
  @Nonnull
  public List<DartFunctionDeclarationWithBody> getFunctionDeclarationWithBodyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartFunctionDeclarationWithBody.class);
  }

  @Override
  @Nonnull
  public List<DartIfStatement> getIfStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartIfStatement.class);
  }

  @Override
  @Nonnull
  public List<DartLabel> getLabelList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartLabel.class);
  }

  @Override
  @Nonnull
  public List<DartRethrowStatement> getRethrowStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartRethrowStatement.class);
  }

  @Override
  @Nonnull
  public List<DartReturnStatement> getReturnStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartReturnStatement.class);
  }

  @Override
  @Nonnull
  public List<DartSwitchStatement> getSwitchStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartSwitchStatement.class);
  }

  @Override
  @Nonnull
  public List<DartThrowStatement> getThrowStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartThrowStatement.class);
  }

  @Override
  @Nonnull
  public List<DartTryStatement> getTryStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartTryStatement.class);
  }

  @Override
  @Nonnull
  public List<DartVarDeclarationList> getVarDeclarationListList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartVarDeclarationList.class);
  }

  @Override
  @Nonnull
  public List<DartWhileStatement> getWhileStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartWhileStatement.class);
  }

}
