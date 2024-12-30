package com.jetbrains.lang.dart.ide.generation;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponent;
import com.jetbrains.lang.dart.psi.DartReturnType;
import com.jetbrains.lang.dart.psi.DartType;
import com.jetbrains.lang.dart.util.DartPresentableUtil;
import consulo.language.editor.template.Template;
import consulo.language.editor.template.TemplateManager;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.util.collection.ContainerUtil;
import consulo.util.lang.function.Condition;

import jakarta.annotation.Nonnull;
import java.util.List;

public class CreateGetterSetterFix extends BaseCreateMethodsFix<DartComponent> {
  public enum Strategy {
    GETTER(DartBundle.message("dart.fix.getter.none.found")) {
      @Override
      boolean accept(final String name, List<DartComponent> componentList) {
        return name.startsWith("_") && ContainerUtil.find(componentList, new consulo.util.lang.function.Condition<DartComponent>() {
          @Override
          public boolean value(DartComponent component) {
            return component.isGetter() && DartPresentableUtil.setterGetterName(name).equals(component.getName());
          }
        }) == null;
      }
    },

    SETTER(DartBundle.message("dart.fix.setter.none.found")) {
      @Override
      boolean accept(final String name, List<DartComponent> componentList) {
        return name.startsWith("_") && ContainerUtil.find(componentList, new Condition<DartComponent>() {
          @Override
          public boolean value(DartComponent component) {
            return component.isSetter() && DartPresentableUtil.setterGetterName(name).equals(component.getName());
          }
        }) == null;
      }
    },

    GETTERSETTER(DartBundle.message("dart.fix.gettersetter.none.found")) {
      @Override
      boolean accept(final String name, List<DartComponent> componentList) {
        return name.startsWith("_") && ContainerUtil.find(componentList, new consulo.util.lang.function.Condition<DartComponent>() {
          @Override
          public boolean value(DartComponent component) {
            return (component.isGetter() || component.isSetter()) && DartPresentableUtil.setterGetterName(name).equals(component
                                                                                                                         .getName());
          }
        }) == null;
      }
    };

    private final String myNothingFoundMessage;

    Strategy(final String nothingFoundMessage) {
      myNothingFoundMessage = nothingFoundMessage;
    }

    abstract boolean accept(String name, List<DartComponent> componentList);
  }

  private final
  @Nonnull
  Strategy myStrategy;

  public CreateGetterSetterFix(final DartClass dartClass, @Nonnull Strategy strategy) {
    super(dartClass);
    myStrategy = strategy;
  }

  @Override
  @Nonnull
  protected String getNothingFoundMessage() {
    return myStrategy.myNothingFoundMessage;
  }

  @Override
  protected Template buildFunctionsText(TemplateManager templateManager, DartComponent namedComponent) {
    final DartReturnType returnType = PsiTreeUtil.getChildOfType(namedComponent, DartReturnType.class);
    final DartType dartType = PsiTreeUtil.getChildOfType(namedComponent, DartType.class);
    final String typeText = returnType == null ? DartPresentableUtil.buildTypeText(namedComponent, dartType,
                                                                                   null) : DartPresentableUtil.buildTypeText(namedComponent,
                                                                                                                             returnType,
                                                                                                                             null);
    final Template template = templateManager.createTemplate(getClass().getName(), DART_TEMPLATE_GROUP);
    template.setToReformat(true);
    if (myStrategy == Strategy.GETTER || myStrategy == Strategy.GETTERSETTER) {
      buildGetter(template, namedComponent.getName(), typeText);
    }
    if (myStrategy == Strategy.SETTER || myStrategy == Strategy.GETTERSETTER) {
      buildSetter(template, namedComponent.getName(), typeText);
    }
    return template;
  }

  private static void buildGetter(Template template, String name, String typeText) {
    build(template, name, typeText, true);
  }

  private static void buildSetter(Template template, String name, String typeText) {
    build(template, name, typeText, false);
  }

  private static void build(Template template, String name, String typeText, boolean isGetter) {
    if (isGetter) {
      template.addTextSegment(typeText);
      template.addTextSegment(" ");
    }

    template.addTextSegment(isGetter ? "get" : "set");
    template.addTextSegment(" ");
    template.addEndVariable();
    template.addTextSegment(DartPresentableUtil.setterGetterName(name));
    if (!isGetter) {
      template.addTextSegment("(");
      template.addTextSegment(typeText);
      template.addTextSegment(" value");
      template.addTextSegment(")");
    }
    template.addTextSegment(" => ");
    if (isGetter) {
      template.addTextSegment(name);
      template.addTextSegment(";");
    }
    else {
      template.addTextSegment(name);
      template.addTextSegment("=value;\n");
    }
  }
}
