package com.jetbrains.lang.dart.validation.fixes;

import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.DartTokenTypes;
import com.jetbrains.lang.dart.psi.DartComponent;
import consulo.codeEditor.Editor;
import consulo.google.dart.localize.DartLocalize;
import consulo.language.ast.ASTNode;
import consulo.language.ast.TokenType;
import consulo.language.psi.PsiElement;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class MakeStaticAction extends BaseCreateFix {
  private final DartComponent myComponent;

  public MakeStaticAction(@Nonnull DartComponent component) {
    myComponent = component;
  }

  @Nonnull
  @Override
  public LocalizeValue getName() {
    DartComponentType componentType = DartComponentType.typeOf(myComponent);
    String componentTypeString = componentType == null ? "" : componentType.toString().toLowerCase();
    return DartLocalize.dartMakeStaticFixName(myComponent.getName(), componentTypeString);
  }

  @Override
  protected void applyFix(Project project, @Nonnull PsiElement psiElement, @Nullable Editor editor) {
    ASTNode node = myComponent.getNode();
    ASTNode anchor = node.getFirstChildNode();
    node.addLeaf(DartTokenTypes.STATIC, DartTokenTypes.STATIC.toString(), anchor);
    node.addLeaf(TokenType.WHITE_SPACE, " ", anchor);
  }
}
