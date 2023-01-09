package com.jetbrains.lang.dart.util;

import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.psi.*;
import consulo.codeEditor.Editor;
import consulo.language.ast.ASTNode;
import consulo.language.editor.PsiEquivalenceUtil;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiWhiteSpace;
import consulo.language.psi.resolve.ResolveState;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.navigation.NavigationItem;
import consulo.project.Project;
import consulo.util.collection.ContainerUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author: Fedor.Korotkov
 */
public class DartRefactoringUtil {
  public static Set<String> collectUsedNames(PsiElement context) {
    return new HashSet<String>(ContainerUtil.map(collectUsedComponents(context), NavigationItem::getName));
  }

  public static Set<DartComponentName> collectUsedComponents(PsiElement context) {
    final Set<DartComponentName> usedComponentNames = new HashSet<DartComponentName>();
    PsiTreeUtil.treeWalkUp(new ComponentNameScopeProcessor(usedComponentNames), context, null, new ResolveState());
    return usedComponentNames;
  }

  @Nullable
  public static DartExpression getSelectedExpression(@Nonnull final Project project,
                                                     @Nonnull PsiFile file,
                                                     @Nonnull final PsiElement element1,
                                                     @Nonnull final PsiElement element2) {
    PsiElement parent = PsiTreeUtil.findCommonParent(element1, element2);
    if (parent == null) {
      return null;
    }
    if (parent instanceof DartExpression) {
      return (DartExpression)parent;
    }
    return PsiTreeUtil.getParentOfType(parent, DartExpression.class);
  }

  @Nonnull
  public static List<PsiElement> getOccurrences(@Nonnull final PsiElement pattern, @Nullable final PsiElement context) {
    if (context == null) {
      return Collections.emptyList();
    }
    final List<PsiElement> occurrences = new ArrayList<PsiElement>();
    context.acceptChildren(new DartRecursiveVisitor() {
      public void visitElement(@Nonnull final PsiElement element) {
        if (DartComponentType.typeOf(element) == DartComponentType.PARAMETER) {
          return;
        }
        if (PsiEquivalenceUtil.areElementsEquivalent(element, pattern)) {
          occurrences.add(element);
          return;
        }
        super.visitElement(element);
      }
    });
    return occurrences;
  }

  @Nullable
  public static PsiElement findOccurrenceUnderCaret(List<PsiElement> occurrences, Editor editor) {
    if (occurrences.isEmpty()) {
      return null;
    }
    int offset = editor.getCaretModel().getOffset();
    for (PsiElement occurrence : occurrences) {
      if (occurrence.getTextRange().contains(offset)) {
        return occurrence;
      }
    }
    int line = editor.getDocument().getLineNumber(offset);
    for (PsiElement occurrence : occurrences) {
      if (occurrence.isValid() && editor.getDocument().getLineNumber(occurrence.getTextRange().getStartOffset()) == line) {
        return occurrence;
      }
    }
    for (PsiElement occurrence : occurrences) {
      if (occurrence.isValid()) {
        return occurrence;
      }
    }
    return null;
  }

  public static PsiElement[] findStatementsInRange(PsiFile file, int startOffset, int endOffset) {
    PsiElement element1 = file.findElementAt(startOffset);
    PsiElement element2 = file.findElementAt(endOffset - 1);
    if (element1 instanceof PsiWhiteSpace) {
      startOffset = element1.getTextRange().getEndOffset();
      element1 = file.findElementAt(startOffset);
    }
    if (element2 instanceof PsiWhiteSpace) {
      endOffset = element2.getTextRange().getStartOffset();
      element2 = file.findElementAt(endOffset - 1);
    }

    if (element1 != null && element2 != null) {
      PsiElement commonParent = PsiTreeUtil.findCommonParent(element1, element2);
      if (commonParent instanceof DartExpression) {
        return new PsiElement[]{commonParent};
      }
    }

    final DartStatements statements = PsiTreeUtil.getParentOfType(element1, DartStatements.class);
    if (statements == null || element1 == null || element2 == null || !PsiTreeUtil.isAncestor(statements, element2, true)) {
      return PsiElement.EMPTY_ARRAY;
    }

    // don't forget about leafs (ex. ';')
    final ASTNode[] astResult = UsefulPsiTreeUtil.findChildrenRange(statements.getNode().getChildren(null), startOffset, endOffset);
    return ContainerUtil.map2Array(astResult, PsiElement.class, node -> node.getPsi());
  }

  @Nullable
  public static DartExpression findExpressionInRange(PsiFile file, int startOffset, int endOffset) {
    PsiElement element1 = file.findElementAt(startOffset);
    PsiElement element2 = file.findElementAt(endOffset - 1);
    if (element1 instanceof PsiWhiteSpace) {
      startOffset = element1.getTextRange().getEndOffset();
    }
    if (element2 instanceof PsiWhiteSpace) {
      endOffset = element2.getTextRange().getStartOffset();
    }
    DartExpression expression = PsiTreeUtil.findElementOfClassAtRange(file, startOffset, endOffset, DartExpression.class);
    if (expression == null || expression.getTextRange().getEndOffset() != endOffset) return null;
    if (expression instanceof DartReference && expression.getParent() instanceof DartCallExpression) return null;
    return expression;
  }
}
