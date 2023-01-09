package com.jetbrains.lang.dart.resolve;

import com.jetbrains.lang.dart.ide.index.DartShowHideInfo;
import com.jetbrains.lang.dart.psi.DartComponentName;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import consulo.logging.Logger;
import consulo.util.dataholder.Key;
import consulo.util.lang.Pair;
import consulo.virtualFileSystem.VirtualFile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public abstract class DartPsiScopeProcessor implements PsiScopeProcessor {
  private static final Logger LOG = Logger.getInstance(DartResolveProcessor.class.getName());

  private final List<Pair<VirtualFile, DartShowHideInfo>> myShowHideFilters = new ArrayList<Pair<VirtualFile, DartShowHideInfo>>();
  private final Map<VirtualFile, Collection<PsiElement>> myFilteredOutElements = new HashMap<VirtualFile, Collection<PsiElement>>();

  public void importedFileProcessingStarted(final @Nonnull VirtualFile importedFile, final @Nonnull DartShowHideInfo showHideInfo) {
    myShowHideFilters.add(Pair.create(importedFile, showHideInfo));
  }

  public void importedFileProcessingFinished(final @Nonnull VirtualFile importedFile) {
    LOG.assertTrue(myShowHideFilters.size() > 0, importedFile.getPath());
    final Pair<VirtualFile, DartShowHideInfo> removed = myShowHideFilters.remove(myShowHideFilters.size() - 1);
    LOG.assertTrue(importedFile.equals(removed.first), "expected: " + removed.first.getPath() + ", actual: " + importedFile.getPath());
  }

  public void processFilteredOutElementsForImportedFile(final @Nonnull VirtualFile importedFile) {
    // removed now, but may be added again in execute();
    final Collection<PsiElement> elements = myFilteredOutElements.remove(importedFile);
    if (elements != null) {
      for (PsiElement element : elements) {
        execute(element, ResolveState.initial());
      }
    }
  }

  @Override
  public final boolean execute(final @Nonnull PsiElement element, final @Nonnull ResolveState state) {
    if (!(element instanceof DartComponentName)) {
      return true;
    }

    if (isFilteredOut(((DartComponentName)element).getName())) {
      final VirtualFile importedFile = myShowHideFilters.get(myShowHideFilters.size() - 1).first;
      Collection<PsiElement> elements = myFilteredOutElements.get(importedFile);
      if (elements == null) {
        elements = new ArrayList<PsiElement>();
        myFilteredOutElements.put(importedFile, elements);
      }
      elements.add(element);

      return true;
    }

    return doExecute((DartComponentName)element);
  }

  protected abstract boolean doExecute(final @Nonnull DartComponentName dartComponentName);

  @Override
  public <T> T getHint(@Nonnull Key<T> hintKey) {
    return null;
  }

  @Override
  public void handleEvent(@Nonnull Event event, @Nullable Object associated) {
  }

  protected boolean isFilteredOut(final String name) {
    for (Pair<VirtualFile, DartShowHideInfo> filter : myShowHideFilters) {
      if (isFilteredOut(name, filter.second)) {
        return true;
      }
    }
    return false;
  }

  private static boolean isFilteredOut(final @Nullable String name, final @Nonnull DartShowHideInfo showHideInfo) {
    if (showHideInfo.getHideComponents().contains(name)) {
      return true;
    }
    if (!showHideInfo.getShowComponents().isEmpty() && !showHideInfo.getShowComponents().contains(name)) {
      return true;
    }
    return false;
  }
}
