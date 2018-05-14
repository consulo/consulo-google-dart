package com.jetbrains.lang.dart.validation.fixes;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.DartTokenTypes;
import com.jetbrains.lang.dart.psi.DartComponent;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MakeStaticAction extends BaseCreateFix {
  private final DartComponent myComponent;

  public MakeStaticAction(@Nonnull DartComponent component) {
    myComponent = component;
  }

  @Nonnull
  @Override
  public String getName() {
    DartComponentType componentType = DartComponentType.typeOf(myComponent);
    String componentTypeString = componentType == null ? "" : componentType.toString().toLowerCase();
    return DartBundle.message("dart.make.static.fix.name", myComponent.getName(), componentTypeString);
  }

  @Override
  protected void applyFix(Project project, @Nonnull PsiElement psiElement, @Nullable Editor editor) {
    ASTNode node = myComponent.getNode();
    ASTNode anchor = node.getFirstChildNode();
    node.addLeaf(DartTokenTypes.STATIC, DartTokenTypes.STATIC.toString(), anchor);
    node.addLeaf(TokenType.WHITE_SPACE, " ", anchor);
  }
}
