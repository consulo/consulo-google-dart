package com.jetbrains.lang.dart.psi;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.List;

public interface DartClass extends DartComponent {
  @Nullable
  DartType getSuperClass();

  @Nonnull
  List<DartType> getImplementsList();

  @Nonnull
  List<DartType> getMixinsList();

  boolean isGeneric();

  @Nonnull
  List<DartComponent> getMethods();

  @Nonnull
  List<DartComponent> getFields();

  @Nonnull
  List<DartComponent> getConstructors();

  @Nullable
  DartComponent findFieldByName(@Nonnull final String name);

  @Nullable
  DartComponent findMethodByName(@Nonnull final String name);

  @Nullable
  DartComponent findMemberByName(@Nonnull final String name);

  @Nonnull
  List<DartComponent> findMembersByName(@Nonnull final String name);

  @Nullable
  DartTypeParameters getTypeParameters();

  @Nullable
  DartOperator findOperator(String operator, @Nullable DartClass rightDartClass);

  List<DartOperator> getOperators();

  @Nullable
  DartComponent findNamedConstructor(String name);
}
