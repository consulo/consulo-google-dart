// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi;

import java.util.List;

import javax.annotation.*;

public interface DartMethodDeclaration extends DartComponent {

  @Nonnull
  DartComponentName getComponentName();

  @Nonnull
  DartFormalParameterList getFormalParameterList();

  @Nullable
  DartFunctionBody getFunctionBody();

  @Nullable
  DartInitializers getInitializers();

  @Nonnull
  List<DartMetadata> getMetadataList();

  @Nullable
  DartRedirection getRedirection();

  @Nullable
  DartReturnType getReturnType();

  @Nullable
  DartStringLiteralExpression getStringLiteralExpression();

}
