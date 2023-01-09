package com.jetbrains.lang.dart.validation.fixes;

import com.jetbrains.lang.dart.util.DartPresentableUtil;
import consulo.language.editor.intention.IntentionAction;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.jetbrains.lang.dart.validation.fixes.DartFixesUtil.findFixesForUnresolved;
import static com.jetbrains.lang.dart.validation.fixes.DartFixesUtil.isStaticContext;

public enum DartTypeErrorCode {
  UNDEFINED_FUNCTION {
    @Nonnull
    @Override
    public List<? extends IntentionAction> getFixes(@Nonnull PsiFile file, int startOffset, @Nonnull String message) {
      // cannot resolve %s
      return findFixesForUnresolved(file, startOffset);
    }
  },
  NOT_A_MEMBER_OF {
    @Nonnull
    @Override
    public List<? extends IntentionAction> getFixes(@Nonnull PsiFile file, int startOffset, @Nonnull String message) {
      // "%s" is not a member of %s
      return findFixesForUnresolved(file, startOffset);
    }
  },
  EXTRA_ARGUMENT {
    @Nonnull
    @Override
    public List<? extends IntentionAction> getFixes(@Nonnull PsiFile file, int startOffset, @Nonnull String message) {
      // todo: extra argument
      return Collections.emptyList();
    }
  },
  UNDEFINED_GETTER {
    @Nonnull
    @Override
    public List<? extends IntentionAction> getFixes(@Nonnull PsiFile file, int startOffset, @Nonnull String message) {
      // Field '%s' has no getter
      PsiElement elementAt = file.findElementAt(startOffset);
      return elementAt == null ? Collections.<IntentionAction>emptyList() : Arrays.asList(new CreateDartGetterSetterAction(elementAt.getText(),
                                                                                                                           true,
                                                                                                                           isStaticContext(
                                                                                                                             file,
                                                                                                                             startOffset)));
    }
  },
  UNDEFINED_SETTER {
    @Nonnull
    @Override
    public List<? extends IntentionAction> getFixes(@Nonnull PsiFile file, int startOffset, @Nonnull String message) {
      // Field '%s' has no setter
      PsiElement elementAt = file.findElementAt(startOffset);
      return elementAt == null ? Collections.<IntentionAction>emptyList() : Arrays.asList(new CreateDartGetterSetterAction(elementAt.getText(),
                                                                                                                           false,
                                                                                                                           isStaticContext(
                                                                                                                             file,
                                                                                                                             startOffset)));
    }
  },
  UNDEFINED_METHOD {
    @Nonnull
    @Override
    public List<? extends IntentionAction> getFixes(@Nonnull PsiFile file, int startOffset, @Nonnull String message) {
      // The method '%s' is not defined for the class '%s'
      String functionName = DartPresentableUtil.findFirstQuotedWord(message);
      if (functionName == null) {
        return Collections.emptyList();
      }
      return Arrays.asList(new CreateDartMethodAction(functionName, isStaticContext(file, startOffset)));
    }
  },
  NO_SUCH_NAMED_PARAMETER {
    @Nonnull
    @Override
    public List<? extends IntentionAction> getFixes(@Nonnull PsiFile file, int startOffset, @Nonnull String message) {
      // todo:  no such named parameter %s defined
      return Collections.emptyList();
    }
  },
  PLUS_CANNOT_BE_USED_FOR_STRING_CONCAT {
    @Nonnull
    @Override
    public List<? extends IntentionAction> getFixes(@Nonnull PsiFile file, int startOffset, @Nonnull String message) {
      // todo:  '%s' cannot be used for string concatentation, use string interpolation or a StringBuffer instead
      return Collections.emptyList();
    }
  },
  STATIC_MEMBER_ACCESSED_THROUGH_INSTANCE {
    @Nonnull
    @Override
    public List<? extends IntentionAction> getFixes(@Nonnull PsiFile file, int startOffset, @Nonnull String message) {
      // todo:  static member %s of %s cannot be accessed through an instance
      return Collections.emptyList();
    }
  },
  UNDEFINED_OPERATOR {
    @Nonnull
    @Override
    public List<? extends IntentionAction> getFixes(@Nonnull PsiFile file, int startOffset, @Nonnull String message) {
      // There is no such operator '%s' in '%s'
      String operator = DartPresentableUtil.findFirstQuotedWord(message);
      return operator == null ? Collections.<IntentionAction>emptyList() : Arrays.asList(new CreateDartOperatorAction(operator));
    }
  };

  @Nonnull
  public abstract List<? extends IntentionAction> getFixes(@Nonnull PsiFile file, int startOffset, @Nonnull String message);

  public static DartTypeErrorCode findError(String code) {
    try {
      return valueOf(code);
    }
    catch (IllegalArgumentException e) {
      return null;
    }
  }
}
