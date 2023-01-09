package com.jetbrains.lang.dart.ide.annotator;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.psi.DartReferenceExpression;
import com.jetbrains.lang.dart.psi.DartVisitor;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import consulo.language.editor.annotation.AnnotationHolder;
import consulo.language.editor.annotation.Annotator;
import consulo.language.psi.PsiElement;

import javax.annotation.Nonnull;

public class DartUnresolvedReferenceVisitor extends DartVisitor implements Annotator {
  private AnnotationHolder myHolder = null;

  @Override
  public void annotate(@Nonnull PsiElement element, @Nonnull AnnotationHolder holder) {
    assert myHolder == null;
    myHolder = holder;
    try {
      element.accept(this);
    }
    finally {
      myHolder = null;
    }
  }

  @Override
  public void visitReferenceExpression(@Nonnull DartReferenceExpression reference) {
    final String referenceText = reference.getText();
    final boolean isSimpleReference = referenceText != null && !"void".equals(referenceText) && !referenceText.contains(".");
    final boolean isPrefix = referenceText != null && DartResolveUtil.getImportedFileByImportPrefix(reference.getContainingFile(),
                                                                                                    referenceText) != null;
    if (isSimpleReference && !isPrefix && reference.resolve() == null) {
      myHolder.createErrorAnnotation(reference, DartBundle.message("cannot.resolve.reference"));
    }
    super.visitReferenceExpression(reference);
  }
}
