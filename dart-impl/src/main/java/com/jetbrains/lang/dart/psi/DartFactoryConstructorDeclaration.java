// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi;

import java.util.List;

import javax.annotation.*;

public interface DartFactoryConstructorDeclaration extends DartComponent {

  @Nullable
  DartComponentName getComponentName();

  @Nonnull
  List<DartExpression> getExpressionList();

  @Nullable
  DartFormalParameterList getFormalParameterList();

  @Nullable
  DartFunctionBody getFunctionBody();

  @Nonnull
  List<DartMetadata> getMetadataList();

  @Nullable
  DartType getType();

}
