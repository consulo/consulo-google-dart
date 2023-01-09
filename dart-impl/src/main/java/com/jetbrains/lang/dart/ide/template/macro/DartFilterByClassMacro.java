package com.jetbrains.lang.dart.ide.template.macro;

import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponentName;
import com.jetbrains.lang.dart.util.DartClassResolveResult;
import com.jetbrains.lang.dart.util.DartRefactoringUtil;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import consulo.language.editor.template.Expression;
import consulo.language.editor.template.ExpressionContext;
import consulo.language.editor.template.PsiElementResult;
import consulo.language.editor.template.Result;
import consulo.language.editor.template.macro.Macro;
import consulo.language.psi.PsiElement;
import consulo.util.collection.ContainerUtil;
import consulo.util.lang.function.Condition;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

/**
 * @author: Fedor.Korotkov
 */
public abstract class DartFilterByClassMacro extends Macro {
  @Override
  public Result calculateResult(@Nonnull Expression[] params, ExpressionContext context) {
    final PsiElement at = context.getPsiElementAtStartOffset();
    final Set<DartComponentName> variables = DartRefactoringUtil.collectUsedComponents(at);
    final List<DartComponentName> filtered = ContainerUtil.filter(variables, new Condition<DartComponentName>() {
      @Override
      public boolean value(DartComponentName name) {
        final PsiElement nameParent = name.getParent();
        if (nameParent instanceof DartClass) {
          return false;
        }
        final DartClassResolveResult result = DartResolveUtil.getDartClassResolveResult(nameParent);
        final DartClass dartClass = result.getDartClass();
        return dartClass != null && filter(dartClass);
      }
    });
    return filtered.isEmpty() ? null : new PsiElementResult(filtered.iterator().next());
  }

  protected abstract boolean filter(@Nonnull DartClass dartClass);
}
