package com.jetbrains.lang.dart.ide.index;

import java.util.Set;

import org.jetbrains.annotations.NotNull;

public interface DartShowHideInfo
{
	@NotNull
	Set<String> getShowComponents();

	@NotNull
	Set<String> getHideComponents();
}
