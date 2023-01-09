package com.jetbrains.lang.dart.ide;

import com.jetbrains.lang.dart.ide.index.DartSymbolIndex;
import com.jetbrains.lang.dart.psi.DartComponentName;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.util.function.Processor;
import consulo.content.scope.SearchScope;
import consulo.ide.navigation.GotoSymbolContributor;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.search.FindSymbolParameters;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.stub.IdFilter;
import consulo.navigation.NavigationItem;
import consulo.project.Project;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class DartSymbolContributor implements GotoSymbolContributor {
  @Override
  public void processNames(@Nonnull Processor<String> processor, @Nonnull SearchScope searchScope, @Nullable IdFilter idFilter) {
    FileBasedIndex.getInstance().processAllKeys(DartSymbolIndex.DART_SYMBOL_INDEX, processor, searchScope, idFilter);
  }

  @Override
  public void processElementsWithName(@Nonnull String name,
                                      @Nonnull Processor<NavigationItem> processor,
                                      @Nonnull FindSymbolParameters findSymbolParameters) {
    Project project = findSymbolParameters.getProject();
    boolean includeNonProjectItems = findSymbolParameters.isSearchInLibraries();
    final GlobalSearchScope scope = includeNonProjectItems ? GlobalSearchScope.allScope(project) : GlobalSearchScope.projectScope(project);
    final Collection<DartComponentName> result = DartSymbolIndex.getItemsByName(name, project, scope);
    for (DartComponentName dartComponentName : result) {
      if (!processor.test(dartComponentName)) {
        break;
      }
    }
  }
}
