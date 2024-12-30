package com.jetbrains.lang.dart.psi.impl;

import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.psi.DartComponent;
import com.jetbrains.lang.dart.psi.DartId;
import com.jetbrains.lang.dart.psi.DartNamedElement;
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

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author: Fedor.Korotkov
 */
public abstract class DartNamedElementImpl extends DartPsiCompositeElementImpl implements DartNamedElement {
  public DartNamedElementImpl(@Nonnull ASTNode node) {
    super(node);
  }

  @Override
  public PsiElement setName(@NonNls @Nonnull String newElementName) throws IncorrectOperationException {
    final DartId identifier = getId();
    final DartId identifierNew = DartElementGenerator.createIdentifierFromText(getProject(), newElementName);

    if (identifierNew != null) {
      getNode().replaceChild(identifier.getNode(), identifierNew.getNode());
    }

    return this;
  }

  @Override
  public String getName() {
    return getId().getText();
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
  public DartId getId() {
    return PsiTreeUtil.getChildOfType(this, DartId.class);
  }
}
