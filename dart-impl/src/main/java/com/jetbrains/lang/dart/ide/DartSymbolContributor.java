package com.jetbrains.lang.dart.ide;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ArrayUtil;
import com.jetbrains.lang.dart.ide.index.DartSymbolIndex;
import com.jetbrains.lang.dart.psi.DartComponentName;
import javax.annotation.Nonnull;

import java.util.Collection;

/**
 * @author: Fedor.Korotkov
 */
public class DartSymbolContributor implements ChooseByNameContributor {
  @Nonnull
  @Override
  public String[] getNames(Project project, boolean includeNonProjectItems) {
    final Collection<String> result = DartSymbolIndex.getNames(project);
    return ArrayUtil.toStringArray(result);
  }

  @Nonnull
  @Override
  public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
    final GlobalSearchScope scope = includeNonProjectItems ? GlobalSearchScope.allScope(project) : GlobalSearchScope.projectScope(project);
    final Collection<DartComponentName> result = DartSymbolIndex.getItemsByName(name, project, scope);
    return result.toArray(new NavigationItem[result.size()]);
  }
}
