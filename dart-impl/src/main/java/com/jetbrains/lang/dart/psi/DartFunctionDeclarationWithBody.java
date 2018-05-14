// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi;

import java.util.List;

import javax.annotation.*;

public interface DartFunctionDeclarationWithBody extends DartComponent {

  @Nonnull
  DartComponentName getComponentName();

  @Nonnull
  DartFormalParameterList getFormalParameterList();

  @Nonnull
  DartFunctionBody getFunctionBody();

  @Nonnull
  List<DartMetadata> getMetadataList();

  @Nullable
  DartReturnType getReturnType();

}
