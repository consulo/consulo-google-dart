package com.jetbrains.lang.dart.psi;

import consulo.language.psi.PsiNameIdentifierOwner;

import jakarta.annotation.Nullable;

public interface DartComponent extends DartPsiCompositeElement, PsiNameIdentifierOwner {
  @Nullable
  DartComponentName getComponentName();

  boolean isStatic();

  boolean isPublic();

  boolean isConstructor();

  boolean isGetter();

  boolean isSetter();

  boolean isAbstract();

  DartMetadata getMetadataByName(final String name);
}
