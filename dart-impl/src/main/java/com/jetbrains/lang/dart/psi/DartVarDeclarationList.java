// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi;

import java.util.List;

import javax.annotation.*;

public interface DartVarDeclarationList extends DartPsiCompositeElement {

  @Nonnull
  DartVarAccessDeclaration getVarAccessDeclaration();

  @Nonnull
  List<DartVarDeclarationListPart> getVarDeclarationListPartList();

  @Nullable
  DartVarInit getVarInit();

}
