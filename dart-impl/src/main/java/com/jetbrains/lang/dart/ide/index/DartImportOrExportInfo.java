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

	@Override
	public boolean equals(Object o)
	{
		if(this == o)
		{
			return true;
		}
		if(o == null || getClass() != o.getClass())
		{
			return false;
		}

		DartImportOrExportInfo that = (DartImportOrExportInfo) o;

		if(myKind != that.myKind)
		{
			return false;
		}
		if(!myUri.equals(that.myUri))
		{
			return false;
		}
		if(myImportPrefix != null ? !myImportPrefix.equals(that.myImportPrefix) : that.myImportPrefix != null)
		{
			return false;
		}
		if(!myShowComponents.equals(that.myShowComponents))
		{
			return false;
		}
		if(!myHideComponents.equals(that.myHideComponents))
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = myKind.hashCode();
		result = 31 * result + myUri.hashCode();
		result = 31 * result + (myImportPrefix != null ? myImportPrefix.hashCode() : 0);
		result = 31 * result + myShowComponents.hashCode();
		result = 31 * result + myHideComponents.hashCode();
		return result;
	}
}
