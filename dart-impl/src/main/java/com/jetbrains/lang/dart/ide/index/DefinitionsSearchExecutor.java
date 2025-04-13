package com.jetbrains.lang.dart.ide.index;

import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponent;
import com.jetbrains.lang.dart.psi.DartComponentName;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.ApplicationManager;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.search.DefinitionsScopedSearch;
import consulo.language.psi.search.DefinitionsScopedSearchExecutor;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

@ExtensionImpl
public class DefinitionsSearchExecutor implements DefinitionsScopedSearchExecutor {
  @Override
  public boolean execute(@Nonnull final DefinitionsScopedSearch.SearchParameters parameters,
                         @Nonnull final Predicate<? super PsiElement> consumer) {
    return ApplicationManager.getApplication().runReadAction(new Supplier<Boolean>() {
      public Boolean get() {
        PsiElement queryParameters = parameters.getElement();
        final PsiElement queryParametersParent = queryParameters.getParent();
        DartComponent dartComponent;
        if (queryParameters instanceof DartClass) {
          dartComponent = (DartClass)queryParameters;
        }
        else if (queryParametersParent instanceof DartComponent && queryParameters instanceof DartComponentName) {
          dartComponent = (DartComponent)queryParametersParent;
        }
        else {
          return true;
        }
        if (dartComponent instanceof DartClass) {
          processInheritors((DartClass)dartComponent, queryParameters, consumer);
        }
        else if (DartComponentType.typeOf(dartComponent) == DartComponentType.METHOD) {
          final String nameToFind = dartComponent.getName();
          if (nameToFind == null) {
            return true;
          }

          DartClass dartClass = PsiTreeUtil.getParentOfType(dartComponent, DartClass.class);
          assert dartClass != null;

          processInheritors(dartClass, queryParameters, element -> {
            for (DartComponent subDartNamedComponent : DartResolveUtil.getNamedSubComponents((DartClass)element)) {
              if (nameToFind.equals(subDartNamedComponent.getName())) {
                consumer.test(subDartNamedComponent);
              }
            }
            return true;
          });
        }
        return true;
      }
    });
  }

  private static boolean processInheritors(final DartClass dartClass,
                                           final PsiElement context,
                                           final Predicate<? super PsiElement> consumer) {
    final Set<DartClass> classSet = new HashSet<>();
    final LinkedList<DartClass> namesQueue = new LinkedList<>();
    namesQueue.add(dartClass);
    while (!namesQueue.isEmpty()) {
      final DartClass currentClass = namesQueue.pollFirst();
      final String currentClassName = currentClass.getName();
      if (currentClassName == null || !classSet.add(currentClass)) {
        continue;
      }
      final Collection<VirtualFile> files = FileBasedIndex.getInstance()
                                                          .getContainingFiles(DartInheritanceIndex.DART_INHERITANCE_INDEX,
                                                                              currentClassName,
                                                                              GlobalSearchScope.allScope(context.getProject()));
      for (VirtualFile virtualFile : files) {
        PsiFile psiFile = dartClass.getManager().findFile(virtualFile);
        for (PsiElement root : DartResolveUtil.findDartRoots(psiFile)) {
          for (DartClass subClass : DartResolveUtil.findClassesByParent(currentClass, root)) {
            if (!consumer.test(subClass)) {
              return true;
            }
            namesQueue.add(subClass);
          }
        }
      }
    }
    return true;
  }
}
