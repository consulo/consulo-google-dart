package com.jetbrains.lang.dart.ide.inspections;

import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.psi.*;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.google.dart.localize.DartLocalize;
import consulo.language.Language;
import consulo.language.editor.inspection.LocalInspectionTool;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemHighlightType;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.inspection.localize.InspectionLocalize;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

@ExtensionImpl
public class DartDeprecatedApiUsageInspection extends LocalInspectionTool {

  private static final String DEPRECATED_METADATA = "deprecated";

  @Nonnull
  public LocalizeValue getGroupDisplayName() {
    return InspectionLocalize.inspectionGeneralToolsGroupName();
  }

  @Nullable
  @Override
  public Language getLanguage() {
    return DartLanguage.INSTANCE;
  }

  @Nonnull
  public LocalizeValue getDisplayName() {
    return DartLocalize.dartDeprecatedApiUsage();
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
      @Override
      @RequiredReadAction
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
            holder.registerProblem(referenceExpression, DartLocalize.refIsDeprecated().get(), ProblemHighlightType.LIKE_DEPRECATED,
                                   LocalQuickFix.EMPTY_ARRAY);
          }
          else if (parent instanceof DartMethodDeclaration && ((DartComponent)parent).isConstructor()) {
            final DartClass dartClass = PsiTreeUtil.getParentOfType(parent, DartClass.class);
            if (dartClass != null && dartClass.getMetadataByName(DEPRECATED_METADATA) != null) {
              holder.registerProblem(referenceExpression, DartLocalize.refIsDeprecated().get(),
                                     ProblemHighlightType.LIKE_DEPRECATED, LocalQuickFix.EMPTY_ARRAY);
            }
          }
        }
      }
    };
  }
}
