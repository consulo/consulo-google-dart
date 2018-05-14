package com.jetbrains.lang.dart.ide.index;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.jetbrains.lang.dart.DartComponentType;

/**
 * @author: Fedor.Korotkov
 */
public class DartComponentInfo
{
	@Nonnull
	private final String value;
	@Nullable
	private final DartComponentType type;
	@Nullable
	private final String libraryId;

	public DartComponentInfo(@Nonnull String value, @Nullable DartComponentType type, @Nullable String libraryId)
	{
		this.value = value;
		this.type = type;
		this.libraryId = libraryId;
	}

	@Nonnull
	public String getValue()
	{
		return value;
	}

	@Nullable
	public DartComponentType getType()
	{
		return type;
	}

	@Nullable
	public String getLibraryId()
	{
		return libraryId;
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

		DartComponentInfo that = (DartComponentInfo) o;

		if(!value.equals(that.value))
		{
			return false;
		}
		if(type != that.type)
		{
			return false;
		}
		if(libraryId != null ? !libraryId.equals(that.libraryId) : that.libraryId != null)
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = value.hashCode();
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (libraryId != null ? libraryId.hashCode() : 0);
		return result;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder("DartComponentInfo{");
		sb.append("value='").append(value).append('\'');
		sb.append(", type=").append(type);
		sb.append(", libraryId='").append(libraryId).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
