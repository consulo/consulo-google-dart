package com.jetbrains.lang.dart.psi.impl;

import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.psi.DartComponent;
import com.jetbrains.lang.dart.psi.DartId;
import com.jetbrains.lang.dart.psi.DartQNamedElement;
import com.jetbrains.lang.dart.psi.DartQualifiedComponentName;
import com.jetbrains.lang.dart.util.DartElementGenerator;
import consulo.content.scope.SearchScope;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.scope.LocalSearchScope;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.navigation.ItemPresentation;
import consulo.navigation.NavigationItem;
import consulo.language.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author: Fedor.Korotkov
 */
public abstract class DartQNamedElementImpl extends DartPsiCompositeElementImpl implements DartQNamedElement {
  public DartQNamedElementImpl(@Nonnull ASTNode node) {
    super(node);
  }

  @Override
  public PsiElement setName(@NonNls @Nonnull String newElementName) throws IncorrectOperationException {
    final DartQualifiedComponentName identifierNew = DartElementGenerator.createQIdentifierFromText(getProject(), newElementName);

    if (identifierNew != null) {
      getNode().replaceAllChildrenToChildrenOf(identifierNew.getNode());
    }

    return this;
  }

  @Override
  public String getName() {
    StringBuilder name = new StringBuilder();
    for (DartId id : getIds()) {
      if (name.length() > 0) {
        name.append('.');
      }
      name.append(id.getText());
    }
    return name.toString();
  }

  @Nullable
  public ItemPresentation getPresentation() {
    final PsiElement parent = getParent();
    if (parent instanceof NavigationItem) {
      return ((NavigationItem)parent).getPresentation();
    }
    return null;
  }

  @Override
  public PsiElement getNameIdentifier() {
    return this;
  }

  @Nonnull
  @Override
  public SearchScope getUseScope() {
    final DartComponentType type = DartComponentType.typeOf(getParent());
    final DartComponent component = PsiTreeUtil.getParentOfType(getParent(), DartComponent.class, true);
    final boolean localType = type == DartComponentType.FUNCTION
      || type == DartComponentType.PARAMETER
      || type == DartComponentType.VARIABLE;
    if (localType && component != null) {
      return new LocalSearchScope(component);
    }
    return super.getUseScope();
  }

  @Nonnull
  @Override
  public DartId[] getIds() {
    final DartId[] ids = PsiTreeUtil.getChildrenOfType(this, DartId.class);
    assert ids != null;
    return ids;
  }
}
