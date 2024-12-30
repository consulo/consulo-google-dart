package com.jetbrains.lang.dart.ide.refactoring;

import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.ide.refactoring.extract.DartExtractMethodHandler;
import com.jetbrains.lang.dart.ide.refactoring.introduce.DartIntroduceFinalVariableHandler;
import com.jetbrains.lang.dart.ide.refactoring.introduce.DartIntroduceVariableHandler;
import com.jetbrains.lang.dart.psi.DartNamedElement;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.refactoring.RefactoringSupportProvider;
import consulo.language.editor.refactoring.action.RefactoringActionHandler;
import consulo.language.psi.PsiElement;
import consulo.language.psi.scope.LocalSearchScope;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class DartRefactoringSupportProvider extends RefactoringSupportProvider {
  @Override
  public boolean isInplaceRenameAvailable(PsiElement element, PsiElement context) {
    return element instanceof DartNamedElement &&
      element.getUseScope() instanceof LocalSearchScope;
  }

  @Override
  public RefactoringActionHandler getIntroduceVariableHandler() {
    return new DartIntroduceVariableHandler();
  }

  @Nullable
  @Override
  public RefactoringActionHandler getIntroduceConstantHandler() {
    return new DartIntroduceFinalVariableHandler();
  }

  @Nullable
  @Override
  public RefactoringActionHandler getExtractMethodHandler() {
    return new DartExtractMethodHandler();
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return DartLanguage.INSTANCE;
  }
}
