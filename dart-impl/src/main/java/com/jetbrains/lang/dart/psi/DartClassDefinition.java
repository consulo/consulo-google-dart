// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface DartClassDefinition extends DartClass {

  @Nullable
  DartClassBody getClassBody();

  @Nonnull
  DartComponentName getComponentName();

  @Nullable
  DartInterfaces getInterfaces();

  @Nonnull
  List<DartMetadata> getMetadataList();

  @Nullable
  DartMixinApplication getMixinApplication();

  @Nullable
  DartMixins getMixins();

  @Nullable
  DartStringLiteralExpression getStringLiteralExpression();

  @Nullable
  DartSuperclass getSuperclass();

  @Nullable
  DartTypeParameters getTypeParameters();

}
