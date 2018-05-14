// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi;

import java.util.List;

import javax.annotation.*;

public interface DartFunctionSignature extends DartComponent {

  @Nonnull
  DartComponentName getComponentName();

  @Nonnull
  DartFormalParameterList getFormalParameterList();

  @Nonnull
  List<DartMetadata> getMetadataList();

  @Nullable
  DartReturnType getReturnType();

}
