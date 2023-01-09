package com.jetbrains.lang.dart.ide.inspections;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.psi.*;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.inspection.LocalInspectionTool;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemHighlightType;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import consulo.language.psi.util.PsiTreeUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@ExtensionImpl
public class DartDeprecatedApiUsageInspection extends LocalInspectionTool {

  private static final String DEPRECATED_METADATA = "deprecated";

  @Nonnull
  public String getGroupDisplayName() {
    return "General";
  }

  @Nullable
  @Override
  public Language getLanguage() {
    return DartLanguage.INSTANCE;
  }

  @Nonnull
  public String getDisplayName() {
    return DartBundle.message("dart.deprecated.api.usage");
  }

  @Nonnull
  public HighlightDisplayLevel getDefaultLevel() {
    return HighlightDisplayLevel.WEAK_WARNING;
  }

  @Override
  public boolean isEnabledByDefault() {
    return true;
  }

  @Nonnull
  public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, final boolean isOnTheFly) {
    return new DartVisitor() {
      public void visitReferenceExpression(@Nonnull final DartReferenceExpression referenceExpression) {
        if (PsiTreeUtil.getChildOfType(referenceExpression, DartReferenceExpression.class) != null) {
          return;
        }

        final PsiElement referenceParent = referenceExpression.getParent();
        if (referenceParent instanceof DartFactoryConstructorDeclaration || referenceParent instanceof DartNamedConstructorDeclaration) {
          return; // no need to highlight constructor declaration
        }

        final PsiElement resolve = referenceExpression.resolve();
        final PsiElement parent = resolve == null ? null : resolve.getParent();
        if (resolve instanceof DartComponentName && (parent instanceof DartComponent)) {
          if (((DartComponent)parent).getMetadataByName(DEPRECATED_METADATA) != null) {
            holder.registerProblem(referenceExpression, DartBundle.message("ref.is.deprecated"), ProblemHighlightType.LIKE_DEPRECATED,
                                   LocalQuickFix.EMPTY_ARRAY);
          }
          else if (parent instanceof DartMethodDeclaration && ((DartComponent)parent).isConstructor()) {
            final DartClass dartClass = PsiTreeUtil.getParentOfType(parent, DartClass.class);
            if (dartClass != null && dartClass.getMetadataByName(DEPRECATED_METADATA) != null) {
              holder.registerProblem(referenceExpression, DartBundle.message("ref.is.deprecated"),
                                     ProblemHighlightType.LIKE_DEPRECATED, LocalQuickFix.EMPTY_ARRAY);
            }
          }
        }
      }
    };
  }
}
