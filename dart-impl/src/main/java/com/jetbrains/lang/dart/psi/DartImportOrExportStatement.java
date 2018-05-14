package com.jetbrains.lang.dart.psi;

import java.util.List;

import javax.annotation.Nonnull;

public interface DartImportOrExportStatement extends DartPsiCompositeElement
{
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
