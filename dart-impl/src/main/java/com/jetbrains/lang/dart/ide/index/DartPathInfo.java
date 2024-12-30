package com.jetbrains.lang.dart.ide.index;

import com.jetbrains.lang.dart.psi.DartComponentName;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import consulo.util.dataholder.Key;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Set;

/**
 * @author: Fedor.Korotkov
 */
public class DartPathInfo {
  private final String path;
  @Nullable
  private final String prefix;
  private final Set<String> showComponents;
  private final Set<String> hideComponents;

  public DartPathInfo(String path, @Nullable String prefix, Set<String> showComponents, Set<String> hideComponents) {
    this.path = path;
    this.prefix = prefix;
    this.showComponents = showComponents;
    this.hideComponents = hideComponents;
  }

  public String getPath() {
    return path;
  }

  public Set<String> getShowComponents() {
    return showComponents;
  }

  public Set<String> getHideComponents() {
    return hideComponents;
  }

  public PsiScopeProcessor wrapElementProcessor(final PsiScopeProcessor processor) {
    if (showComponents.isEmpty() && hideComponents.isEmpty()) {
      return processor;
    }
    return new PsiScopeProcessor() {
      @Override
      public boolean execute(@Nonnull PsiElement element, ResolveState state) {
        if (element instanceof DartComponentName && isComponentExcluded(((DartComponentName)element).getName())) {
          return true;
        }
        return processor.execute(element, state);
      }

      @Nullable
      @Override
      public <T> T getHint(@Nonnull Key<T> hintKey) {
        return processor.getHint(hintKey);
      }

      @Override
      public void handleEvent(Event event, @Nullable Object associated) {
        processor.handleEvent(event, associated);
      }
    };
  }

  public boolean isComponentExcluded(@Nullable String elementName) {
    // nothing
    if (showComponents.isEmpty() && hideComponents.isEmpty()) {
      return false;
    }
    // hide
    if (hideComponents.contains(elementName)) {
      return true;
    }
    // show isn't empty and doesn't contain name
    if (!showComponents.isEmpty() && !showComponents.contains(elementName)) {
      return true;
    }
    return false;
  }

  public String getPrefix() {
    return prefix;
  }
}
