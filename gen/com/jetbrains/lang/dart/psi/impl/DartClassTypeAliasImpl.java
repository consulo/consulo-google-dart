// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.jetbrains.lang.dart.psi.DartClassTypeAlias;
import com.jetbrains.lang.dart.psi.DartComponentName;
import com.jetbrains.lang.dart.psi.DartMixins;
import com.jetbrains.lang.dart.psi.DartType;
import com.jetbrains.lang.dart.psi.DartTypeParameters;
import com.jetbrains.lang.dart.psi.DartVisitor;

public class DartClassTypeAliasImpl extends TypedefDartPsiClass implements DartClassTypeAlias {

  public DartClassTypeAliasImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitClassTypeAlias(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public DartComponentName getComponentName() {
    return findNotNullChildByClass(DartComponentName.class);
  }

  @Override
  @Nullable
  public DartMixins getMixins() {
    return findChildByClass(DartMixins.class);
  }

  @Override
  @Nullable
  public DartType getType() {
    return findChildByClass(DartType.class);
  }

  @Override
  @Nullable
  public DartTypeParameters getTypeParameters() {
    return findChildByClass(DartTypeParameters.class);
  }

}
