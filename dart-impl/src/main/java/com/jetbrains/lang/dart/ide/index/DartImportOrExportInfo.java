package com.jetbrains.lang.dart.ide.index;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DartImportOrExportInfo implements DartShowHideInfo
{
	public enum Kind
	{
		Import, Export
	}

	private final
	@Nonnull
	Kind myKind;
	private final
	@Nonnull
	String myUri;
	private final
	@Nullable
	String myImportPrefix;
	private final
	@Nonnull
	Set<String> myShowComponents;
	private final
	@Nonnull
	Set<String> myHideComponents;

	public DartImportOrExportInfo(final @Nonnull Kind kind, final @Nonnull String uri, final @Nullable String importPrefix,
			final @Nonnull Set<String> showComponents, final @Nonnull Set<String> hideComponents)
	{
		myKind = kind;
		myUri = uri;
		myImportPrefix = kind == Kind.Export ? null : importPrefix;
		myShowComponents = showComponents;
		myHideComponents = hideComponents;
	}

	@Nonnull
	public String getUri()
	{
		return myUri;
	}

	@Nonnull
	public Kind getKind()
	{
		return myKind;
	}

	@Nullable
	public String getImportPrefix()
	{
		return myImportPrefix;
	}

	@Nonnull
	public Set<String> getShowComponents()
	{
		return myShowComponents;
	}

	@Nonnull
	public Set<String> getHideComponents()
	{
		return myHideComponents;
	}
}
