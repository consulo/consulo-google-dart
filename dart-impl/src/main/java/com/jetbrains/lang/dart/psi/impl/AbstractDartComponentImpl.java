package com.jetbrains.lang.dart.psi.impl;

import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.DartTokenTypes;
import com.jetbrains.lang.dart.psi.*;
import com.jetbrains.lang.dart.util.DartPresentableUtil;
import consulo.language.ast.ASTNode;
import consulo.language.icon.IconDescriptorUpdaters;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.navigation.ItemPresentation;
import consulo.ui.image.Image;
import consulo.language.util.IncorrectOperationException;
import consulo.util.lang.StringUtil;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

abstract public class AbstractDartComponentImpl extends DartPsiCompositeElementImpl implements DartComponent {
  public AbstractDartComponentImpl(@Nonnull ASTNode node) {
    super(node);
  }

  @Override
  public String getName() {
    final DartComponentName name = getComponentName();
    if (name != null) {
      return name.getText();
    }
    return super.getName();
  }

  @Override
  public PsiElement setName(@NonNls @Nonnull String name) throws IncorrectOperationException {
    final DartComponentName componentName = getComponentName();
    if (componentName != null) {
      componentName.setName(name);
    }
    return this;
  }

  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    return getComponentName();
  }

  @Override
  public boolean isStatic() {
    return findChildByType(DartTokenTypes.STATIC) != null;
  }

  @Override
  public boolean isPublic() {
    final String name = getName();
    return name != null && !name.startsWith("_");
  }

  public boolean isConstructor() {
    return DartComponentType.typeOf(this) == DartComponentType.CONSTRUCTOR;
  }

  @Override
  public boolean isSetter() {
    return findChildByType(DartTokenTypes.SET) != null;
  }

  @Override
  public boolean isGetter() {
    return findChildByType(DartTokenTypes.GET) != null;
  }

  @Override
  public boolean isAbstract() {
    final DartComponentType componentType = DartComponentType.typeOf(this);
    return componentType == DartComponentType.CLASS && findChildByType(DartTokenTypes.ABSTRACT) != null || componentType == DartComponentType
      .METHOD && findChildByType(DartTokenTypes.FUNCTION_BODY) == null;
  }

  @Override
  @Nullable
  public DartMetadata getMetadataByName(final String name) {
    for (DartMetadata metadata : PsiTreeUtil.getChildrenOfTypeAsList(this, DartMetadata.class)) {
      if ("deprecated".equals(metadata.getReferenceExpression().getText())) {
        return metadata;
      }
    }
    return null;
  }

  @Override
  public ItemPresentation getPresentation() {
    return new ItemPresentation() {
      @Override
      public String getPresentableText() {
        final StringBuilder result = new StringBuilder();
        result.append(getComponentName());
        final DartComponentType type = DartComponentType.typeOf(AbstractDartComponentImpl.this);
        if ((type == DartComponentType.METHOD || type == DartComponentType.FUNCTION || type == DartComponentType.CONSTRUCTOR) && !(isGetter()
          || isSetter())) {
          final String parameterList = DartPresentableUtil.getPresentableParameterList(AbstractDartComponentImpl.this);
          result.append("(").append(parameterList).append(")");
        }
        if (type == DartComponentType.METHOD || type == DartComponentType.FIELD || type == DartComponentType.FUNCTION) {
          final DartReturnType returnType = PsiTreeUtil.getChildOfType(AbstractDartComponentImpl.this, DartReturnType.class);
          final DartType dartType = PsiTreeUtil.getChildOfType(AbstractDartComponentImpl.this, DartType.class);
          if (returnType != null) {
            result.append(":");
            result.append(DartPresentableUtil.buildTypeText(AbstractDartComponentImpl.this, returnType, null));
          }
          else if (dartType != null) {
            result.append(":");
            result.append(DartPresentableUtil.buildTypeText(AbstractDartComponentImpl.this, dartType, null));
          }
        }
        return result.toString();
      }

      @Nullable
      private String getComponentName() {
        String name = getName();
        if (DartComponentType.typeOf(AbstractDartComponentImpl.this) == DartComponentType.CONSTRUCTOR) {
          DartClass dartClass = PsiTreeUtil.getParentOfType(AbstractDartComponentImpl.this, DartClass.class);
          if (dartClass == null) {
            return name;
          }
          return StringUtil.isEmpty(name) ? dartClass.getName() : dartClass.getName() + "." + name;
        }
        return name;
      }

      @Override
      public String getLocationString() {
        if (!isValid()) {
          return "";
        }
        if (!(AbstractDartComponentImpl.this instanceof DartClass)) {
          final DartClass dartClass = PsiTreeUtil.getParentOfType(AbstractDartComponentImpl.this, DartClass.class);
          if (dartClass != null) {
            return dartClass.getName();
          }
        }
        DartExecutionScope root = PsiTreeUtil.getTopmostParentOfType(AbstractDartComponentImpl.this, DartExecutionScope.class);
        DartPartOfStatement partOfStatement = PsiTreeUtil.getChildOfType(root, DartPartOfStatement.class);
        return partOfStatement == null ? null : partOfStatement.getLibraryName();
      }

      @Override
      public Image getIcon() {
        return IconDescriptorUpdaters.getIcon(AbstractDartComponentImpl.this, 0);
      }
    };
  }
}
