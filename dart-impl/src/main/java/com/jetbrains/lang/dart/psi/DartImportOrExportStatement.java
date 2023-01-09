package com.jetbrains.lang.dart.psi;

import javax.annotation.Nonnull;
import java.util.List;

public interface DartImportOrExportStatement extends DartPsiCompositeElement {
  @Nonnull
  List<DartMetadata> getMetadataList();

  @Nonnull
  DartPathOrLibraryReference getLibraryExpression();

  @Nonnull
  String getUri();

  @Nonnull
  List<DartShowCombinator> getShowCombinatorList();

  @Nonnull
  List<DartHideCombinator> getHideCombinatorList();
}
