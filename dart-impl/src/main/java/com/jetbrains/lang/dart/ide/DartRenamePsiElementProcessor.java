package com.jetbrains.lang.dart.ide;

import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponent;
import com.jetbrains.lang.dart.psi.DartComponentName;
import com.jetbrains.lang.dart.psi.DartNamedConstructorDeclaration;
import consulo.annotation.component.ExtensionImpl;
import consulo.content.scope.SearchScope;
import consulo.language.editor.refactoring.rename.RenamePsiElementProcessor;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class DartRenamePsiElementProcessor extends RenamePsiElementProcessor {
  @Override
  public boolean canProcessElement(@Nonnull PsiElement element) {
    return element instanceof DartComponentName;
  }

  @Override
  public void prepareRenaming(PsiElement element, String newName, Map<PsiElement, String> allRenames, SearchScope scope) {
    super.prepareRenaming(element, newName, allRenames, scope);
    for (PsiElement elementToRename : allRenames.keySet()) {
      final PsiElement parent = elementToRename.getParent();
      if (parent instanceof DartClass) {
        for (DartComponent constructor : ((DartClass)parent).getConstructors()) {
          if (!(constructor instanceof DartNamedConstructorDeclaration)) {
            allRenames.put(constructor.getComponentName(), newName);
          }
        }
      }
      else if (parent instanceof DartComponent) {
        final DartClass dartClass = PsiTreeUtil.getParentOfType(parent, DartClass.class, true);
        final String componentName = ((DartComponent)parent).getName();
        if (componentName != null && dartClass != null && componentName.equals(dartClass.getName())) {
          allRenames.put(dartClass.getComponentName(), newName);
        }
      }
    }
  }
}
