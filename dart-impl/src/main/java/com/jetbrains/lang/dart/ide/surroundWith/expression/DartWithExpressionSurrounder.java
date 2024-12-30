package com.jetbrains.lang.dart.ide.surroundWith.expression;

import com.jetbrains.lang.dart.psi.DartExpression;
import com.jetbrains.lang.dart.util.DartElementGenerator;
import consulo.codeEditor.Editor;
import consulo.document.util.TextRange;
import consulo.language.editor.surroundWith.Surrounder;
import consulo.language.psi.PsiElement;
import consulo.project.Project;
import consulo.language.util.IncorrectOperationException;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author: Fedor.Korotkov
 */
public abstract class DartWithExpressionSurrounder implements Surrounder {
  public boolean isApplicable(@Nonnull PsiElement[] elements) {
    return elements.length == 1 && elements[0] instanceof DartExpression;
  }

  @Nullable
  protected DartExpression getSurroundedNode(@Nonnull final PsiElement element) {
    return DartElementGenerator.createExpressionFromText(
      element.getProject(),
      getTemplateText(element)
    );
  }

  @Nullable
  public TextRange surroundElements(@Nonnull Project project, @Nonnull Editor editor, @Nonnull PsiElement[] elements)
    throws IncorrectOperationException {
    PsiElement source = elements[0];

    final DartExpression parenthExprNode = getSurroundedNode(source);
    if (parenthExprNode == null) {
      throw new IncorrectOperationException("Can't create expression for: " + source.getText());
    }

    final PsiElement replace = source.replace(parenthExprNode);
    final int endOffset = replace.getTextRange().getEndOffset();
    return TextRange.create(endOffset, endOffset);
  }

  protected abstract String getTemplateText(PsiElement expr);
}
