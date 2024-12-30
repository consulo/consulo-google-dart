package com.jetbrains.lang.dart.ide;

import com.jetbrains.lang.dart.ide.index.DartClassIndex;
import com.jetbrains.lang.dart.psi.DartComponentName;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.util.function.Processor;
import consulo.content.scope.SearchScope;
import consulo.ide.navigation.GotoClassOrTypeContributor;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.search.FindSymbolParameters;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.stub.IdFilter;
import consulo.navigation.NavigationItem;
import consulo.project.Project;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Collection;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class DartClassContributor implements GotoClassOrTypeContributor {
  @Override
  public void processNames(@Nonnull Processor<String> processor, @Nonnull SearchScope searchScope, @Nullable IdFilter idFilter) {
    FileBasedIndex.getInstance().processAllKeys(DartClassIndex.DART_CLASS_INDEX, processor, searchScope, idFilter);
  }

  @Override
  public void processElementsWithName(@Nonnull String name,
                                      @Nonnull Processor<NavigationItem> processor,
                                      @Nonnull FindSymbolParameters findSymbolParameters) {
    boolean searchInLibraries = findSymbolParameters.isSearchInLibraries();
    Project project = findSymbolParameters.getProject();
    final GlobalSearchScope scope = searchInLibraries ? GlobalSearchScope.allScope(project) : GlobalSearchScope.projectScope(project);
    final Collection<DartComponentName> result = DartClassIndex.getItemsByName(name, project, scope);

    for (DartComponentName dartComponentName : result) {
      if (!processor.test(dartComponentName)) {
        break;
      }
    }
  }
}
