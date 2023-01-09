package com.jetbrains.lang.dart.ide.structure;

import com.jetbrains.lang.dart.psi.*;
import com.jetbrains.lang.dart.psi.impl.DartPsiCompositeElementImpl;
import com.jetbrains.lang.dart.util.DartClassResolveResult;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import consulo.fileEditor.structureView.StructureViewTreeElement;
import consulo.fileEditor.structureView.tree.SortableTreeElement;
import consulo.fileEditor.structureView.tree.TreeElement;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.ResolveState;
import consulo.navigation.ItemPresentation;
import consulo.navigation.NavigationItem;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: Fedor.Korotkov
 */
public class DartStructureViewElement implements StructureViewTreeElement, SortableTreeElement {
  private final PsiElement myElement;

  public DartStructureViewElement(final PsiElement element) {
    myElement = element;
  }

  @Override
  public Object getValue() {
    return myElement;
  }

  @Override
  public void navigate(boolean requestFocus) {
    if (myElement instanceof NavigationItem) {
      ((NavigationItem)myElement).navigate(requestFocus);
    }
  }

  @Override
  public boolean canNavigate() {
    return myElement instanceof NavigationItem && ((NavigationItem)myElement).canNavigate();
  }

  @Override
  public boolean canNavigateToSource() {
    return myElement instanceof NavigationItem && ((NavigationItem)myElement).canNavigateToSource();
  }

  @Nullable
  @Override
  public ItemPresentation getPresentation() {
    return myElement instanceof NavigationItem ? ((NavigationItem)myElement).getPresentation() : null;
  }

  @Override
  public TreeElement[] getChildren() {
    final List<TreeElement> result = new ArrayList<TreeElement>();
    if (myElement instanceof DartFile || myElement instanceof DartEmbeddedContent) {
      Set<DartComponentName> componentNames = new HashSet<DartComponentName>();
      DartPsiCompositeElementImpl
        .processDeclarationsImpl(myElement, new ComponentNameScopeProcessor(componentNames), ResolveState.initial(), null);
      for (DartComponentName componentName : componentNames) {
        PsiElement parent = componentName.getParent();
        if (parent instanceof DartComponent) {
          result.add(new DartStructureViewElement(parent));
        }
      }
    }
    else if (myElement instanceof DartClass) {
      final DartClass dartClass = (DartClass)myElement;
      final DartClassResolveResult superClass = DartResolveUtil.resolveClassByType(dartClass.getSuperClass());
      if (superClass.getDartClass() != null) {
        result.add(new DartStructureViewElement(superClass.getDartClass()));
      }
      List<DartClassResolveResult> implementsAndMixinsList =
        DartResolveUtil.resolveClassesByTypes(DartResolveUtil.getImplementsAndMixinsList(dartClass));
      for (DartClassResolveResult superInterface : implementsAndMixinsList) {
        if (superInterface.getDartClass() == null) {
          continue;
        }
        result.add(new DartStructureViewElement(superInterface.getDartClass()));
      }
      for (DartComponent subNamedComponent : DartResolveUtil.getNamedSubComponentsInOrder(dartClass)) {
        result.add(new DartStructureViewElement(subNamedComponent));
      }
    }
    return result.toArray(new TreeElement[result.size()]);
  }

  @Override
  public String getAlphaSortKey() {
    final String result = myElement instanceof NavigationItem ? ((NavigationItem)myElement).getName() : null;
    return result == null ? "" : result;
  }

  public PsiElement getRealElement() {
    return myElement;
  }
}
