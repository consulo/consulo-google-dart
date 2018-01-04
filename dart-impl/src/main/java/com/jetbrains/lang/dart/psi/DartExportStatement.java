// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi;

import java.util.List;

import org.jetbrains.annotations.NotNull;

public interface DartExportStatement extends DartImportOrExportStatement {

  @NotNull
  List<DartHideCombinator> getHideCombinatorList();

  @NotNull
  List<DartMetadata> getMetadataList();

  @NotNull
  List<DartShowCombinator> getShowCombinatorList();

	String getUri();

  @NotNull
  DartPathOrLibraryReference getLibraryExpression();

}
