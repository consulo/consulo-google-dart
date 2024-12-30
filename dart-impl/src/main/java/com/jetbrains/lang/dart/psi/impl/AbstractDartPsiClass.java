package com.jetbrains.lang.dart.psi.impl;

import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.psi.*;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.util.collection.ContainerUtil;
import consulo.util.lang.function.Condition;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Collections;
import java.util.List;

abstract public class AbstractDartPsiClass extends AbstractDartComponentImpl implements DartClass {
  public AbstractDartPsiClass(@Nonnull ASTNode node) {
    super(node);
  }

  @Nullable
  @Override
  public DartType getSuperClass() {
    final DartSuperclass superclass = PsiTreeUtil.getChildOfType(this, DartSuperclass.class);
    if (superclass != null) {
      return superclass.getType();
    }

    final DartMixinApplication mixinApp = PsiTreeUtil.getChildOfType(this, DartMixinApplication.class);
    if (mixinApp != null) {
      return mixinApp.getType();
    }

    return null;
  }

  @Nonnull
  @Override
  public List<DartType> getImplementsList() {
    final DartMixinApplication mixinApp = PsiTreeUtil.getChildOfType(this, DartMixinApplication.class);
    final DartInterfaces interfaces = mixinApp != null ? mixinApp.getInterfaces() : PsiTreeUtil.getChildOfType(this, DartInterfaces.class);
    if (interfaces != null) {
      return DartResolveUtil.getTypes(interfaces.getTypeList());
    }

    return Collections.emptyList();
  }

  @Nonnull
  @Override
  public List<DartType> getMixinsList() {
    final DartMixinApplication mixinApp = PsiTreeUtil.getChildOfType(this, DartMixinApplication.class);

    final DartMixins mixins = PsiTreeUtil.getChildOfType(mixinApp != null ? mixinApp : this, DartMixins.class);
    if (mixins != null) {
      return DartResolveUtil.getTypes(mixins.getTypeList());
    }
    return Collections.emptyList();
  }

  @Override
  public boolean isGeneric() {
    return getTypeParameters() != null;
  }

  @Nonnull
  @Override
  public List<DartComponent> getMethods() {
    final List<DartComponent> components = DartResolveUtil.findNamedSubComponents(this);
    return DartResolveUtil.filterComponentsByType(components, DartComponentType.METHOD);
  }

  @Nonnull
  @Override
  public List<DartComponent> getFields() {
    final List<DartComponent> components = DartResolveUtil.findNamedSubComponents(this);
    return DartResolveUtil.filterComponentsByType(components, DartComponentType.FIELD);
  }

  @Nonnull
  @Override
  public List<DartComponent> getConstructors() {
    final List<DartComponent> components = DartResolveUtil.getNamedSubComponents(this);
    final String className = getName();
    if (className == null) {
      return Collections.emptyList();
    }
    return ContainerUtil.filter(components, new consulo.util.lang.function.Condition<DartComponent>() {
      @Override
      public boolean value(DartComponent component) {
        return DartComponentType.typeOf(component) == DartComponentType.CONSTRUCTOR;
      }
    });
  }

  @Override
  public List<DartOperator> getOperators() {
    return DartResolveUtil.findOperators(this);
  }

  @Nullable
  @Override
  public DartOperator findOperator(final String operator, @Nullable final DartClass rightDartClass) {
    return ContainerUtil.find(getOperators(), new Condition<PsiElement>() {
      @Override
      public boolean value(PsiElement element) {
        final DartUserDefinableOperator userDefinableOperator = PsiTreeUtil.getChildOfType(element, DartUserDefinableOperator.class);
        final boolean isGoodOperator = userDefinableOperator != null && operator.equals(DartResolveUtil.getOperatorString
          (userDefinableOperator));
        if (rightDartClass == null) {
          return isGoodOperator;
        }
        final DartFormalParameterList formalParameterList = PsiTreeUtil.getChildOfType(element, DartFormalParameterList.class);
        return isGoodOperator && DartResolveUtil.checkParametersType(formalParameterList, rightDartClass);
      }
    });
  }

  @Override
  public DartComponent findFieldByName(@Nonnull final String name) {
    return ContainerUtil.find(getFields(), new Condition<DartComponent>() {
      @Override
      public boolean value(DartComponent component) {
        return name.equals(component.getName());
      }
    });
  }

  @Override
  public DartComponent findMethodByName(@Nonnull final String name) {
    return ContainerUtil.find(getMethods(), new consulo.util.lang.function.Condition<DartComponent>() {
      @Override
      public boolean value(DartComponent component) {
        return name.equals(component.getName());
      }
    });
  }

  @Override
  public DartComponent findMemberByName(@Nonnull String name) {
    final List<DartComponent> membersByName = findMembersByName(name);
    return membersByName.isEmpty() ? null : membersByName.iterator().next();
  }

  @Nonnull
  @Override
  public List<DartComponent> findMembersByName(@Nonnull final String name) {
    return ContainerUtil.filter(DartResolveUtil.findNamedSubComponents(false, this), new Condition<DartComponent>() {
      @Override
      public boolean value(DartComponent component) {
        final DartClass dartClass = PsiTreeUtil.getParentOfType(component, DartClass.class);
        final String dartClassName = dartClass != null ? dartClass.getName() : null;
        if (dartClassName != null && dartClassName.equals(component.getName())) {
          return false;
        }
        return name.equals(component.getName());
      }
    });
  }

  @Override
  public DartComponent findNamedConstructor(final String name) {
    return ContainerUtil.find(getConstructors(), new Condition<DartComponent>() {
      @Override
      public boolean value(DartComponent component) {
        return name.equals(component.getName());
      }
    });
  }
}
