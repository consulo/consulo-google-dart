package com.jetbrains.lang.dart.ide.index;

import jakarta.annotation.Nonnull;
import java.util.Set;

public interface DartShowHideInfo {
  @Nonnull
  Set<String> getShowComponents();

  @Nonnull
  Set<String> getHideComponents();
}
