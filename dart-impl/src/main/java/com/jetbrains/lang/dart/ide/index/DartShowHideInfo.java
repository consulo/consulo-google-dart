package com.jetbrains.lang.dart.ide.index;

import java.util.Set;

import javax.annotation.Nonnull;

public interface DartShowHideInfo
{
	@Nonnull
	Set<String> getShowComponents();

	@Nonnull
	Set<String> getHideComponents();
}
