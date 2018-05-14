// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface DartImportStatement extends DartImportOrExportStatement {

  @Nonnull
  List<DartHideCombinator> getHideCombinatorList();

  @Nonnull
  List<DartMetadata> getMetadataList();

  @Nonnull
  List<DartShowCombinator> getShowCombinatorList();

	String getUri();

  @Nonnull
  DartPathOrLibraryReference getLibraryExpression();

  @Nullable
  DartComponentName getImportPrefix();

}
