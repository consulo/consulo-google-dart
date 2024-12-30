package com.jetbrains.lang.dart.ide.surroundWith.statement;

import com.jetbrains.lang.dart.util.DartElementGenerator;
import consulo.codeEditor.Editor;
import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.editor.surroundWith.Surrounder;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiParserFacade;
import consulo.project.Project;
import consulo.language.util.IncorrectOperationException;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author: Fedor.Korotkov
 */
public abstract class DartStatementSurrounder implements Surrounder {
  public boolean isApplicable(@Nonnull PsiElement[] elements) {
    return true;
  }

  @Nullable
  protected PsiElement createSurrounder(@Nonnull Project project) {
    return DartElementGenerator.createStatementFromText(
      project,
      getTemplateText()
    );
  }

  @Nullable
  public TextRange surroundElements(@Nonnull Project project, @Nonnull Editor editor, @Nonnull PsiElement[] elements)
    throws IncorrectOperationException {
    PsiElement parent = elements[0].getParent();

    PsiElement surrounder = createSurrounder(project);
    if (surrounder == null) {
      throw new IncorrectOperationException("Can't surround statements!");
    }

    surrounder = parent.addBefore(surrounder, elements[0]);
    final PsiElement elementToAdd = findElementToAdd(surrounder);

    if (elementToAdd == null) {
      parent.deleteChildRange(surrounder, surrounder);
      throw new IncorrectOperationException("Can't surround statements!");
    }

    for (PsiElement element : elements) {
      ASTNode node = element.getNode();
      final ASTNode copyNode = node.copyElement();

      parent.getNode().removeChild(node);
      elementToAdd.getNode().addChild(copyNode);
    }

    afterAdd(elementToAdd);
    int endOffset = cleanUpAndGetPlaceForCaret(surrounder);
    return TextRange.create(endOffset, endOffset);
  }

  @SuppressWarnings("MethodMayBeStatic")
  protected void afterAdd(PsiElement elementToAdd) {
    final PsiElement newLineNode = PsiParserFacade.SERVICE.getInstance(elementToAdd.getProject()).createWhiteSpaceFromText("\n");
    elementToAdd.add(newLineNode);
  }

  protected abstract String getTemplateText();

  @Nullable
  protected abstract PsiElement findElementToAdd(@Nonnull final PsiElement surrounder);

  protected abstract int cleanUpAndGetPlaceForCaret(@Nonnull PsiElement surrounder);
}

