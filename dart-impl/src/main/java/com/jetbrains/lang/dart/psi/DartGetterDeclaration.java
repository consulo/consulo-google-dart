// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi;

import java.util.List;

import javax.annotation.*;

public interface DartGetterDeclaration extends DartComponent {

  @Nonnull
  DartComponentName getComponentName();

  @Nullable
  DartFormalParameterList getFormalParameterList();

  @Nullable
  DartFunctionBody getFunctionBody();

  @Nonnull
  List<DartMetadata> getMetadataList();

  @Nullable
  DartReturnType getReturnType();

  @Nullable
  DartStringLiteralExpression getStringLiteralExpression();

}
