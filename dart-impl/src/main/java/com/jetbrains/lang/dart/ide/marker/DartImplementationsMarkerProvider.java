package com.jetbrains.lang.dart.ide.marker;

import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.ide.index.DartInheritanceIndex;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponent;
import com.jetbrains.lang.dart.psi.DartComponentName;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.AllIcons;
import consulo.codeEditor.markup.GutterIconRenderer;
import consulo.language.Language;
import consulo.language.editor.DaemonBundle;
import consulo.language.editor.Pass;
import consulo.language.editor.gutter.GutterIconNavigationHandler;
import consulo.language.editor.gutter.LineMarkerInfo;
import consulo.language.editor.gutter.LineMarkerProvider;
import consulo.language.editor.ui.DefaultPsiElementCellRenderer;
import consulo.language.editor.ui.PsiElementListNavigator;
import consulo.language.psi.NavigatablePsiElement;
import consulo.language.psi.PsiElement;
import consulo.util.collection.ContainerUtil;
import consulo.util.lang.function.Condition;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ExtensionImpl
public class DartImplementationsMarkerProvider implements LineMarkerProvider {

  @Override
  public LineMarkerInfo getLineMarkerInfo(@Nonnull PsiElement element) {
    return null;
  }

  @Override
  public void collectSlowLineMarkers(@Nonnull List<PsiElement> elements, @Nonnull Collection<LineMarkerInfo> result) {
    elements = ContainerUtil.filter(elements, new Condition<PsiElement>() {
      @Override
      public boolean value(PsiElement element) {
        return element instanceof DartClass;
      }
    });
    if (elements.size() > 20) {
      return;
    }
    for (PsiElement dartClass : elements) {
      collectMarkers(result, (DartClass)dartClass);
    }
  }

  private static void collectMarkers(Collection<LineMarkerInfo> result, DartClass dartClass) {
    final List<DartClass> subClasses = DartInheritanceIndex.getItemsByName(dartClass);
    if (!subClasses.isEmpty()) {
      result.add(createImplementationMarker(dartClass, subClasses));
    }
    final List<DartComponent> subItems = new ArrayList<DartComponent>();
    for (DartClass subClass : subClasses) {
      subItems.addAll(DartResolveUtil.getNamedSubComponents(subClass));
    }
    for (DartComponent dartComponent : DartResolveUtil.getNamedSubComponents(dartClass)) {
      final LineMarkerInfo markerInfo = tryCreateImplementationMarker(dartComponent, subItems, dartComponent.isAbstract());
      if (markerInfo != null) {
        result.add(markerInfo);
      }
    }
  }

  private static LineMarkerInfo createImplementationMarker(final DartClass dartClass, final List<DartClass> items) {
    final DartComponentName componentName = dartClass.getComponentName();
    return new LineMarkerInfo<PsiElement>(componentName, componentName.getTextRange(), AllIcons.Gutter.OverridenMethod, Pass.UPDATE_ALL,
                                          element -> DaemonBundle.message("method.is.implemented.too.many"), new GutterIconNavigationHandler<PsiElement>() {
      @Override
      public void navigate(MouseEvent e, PsiElement elt) {
        PsiElementListNavigator.openTargets(e,
                                            DartResolveUtil.getComponentNames(items).toArray(new NavigatablePsiElement[items.size()]),
                                            DaemonBundle.message("navigation.title.subclass", dartClass.getName(), items.size()),
                                            "Superclasses of " + dartClass.getName
                                              (),
                                            new DefaultPsiElementCellRenderer());
      }
    }, GutterIconRenderer.Alignment.RIGHT
    );
  }

  @Nullable
  private static LineMarkerInfo tryCreateImplementationMarker(final DartComponent componentWithDeclarationList,
                                                              List<DartComponent> subItems,
                                                              final boolean isInterface) {
    final PsiElement componentName = componentWithDeclarationList.getComponentName();
    final String methodName = componentWithDeclarationList.getName();
    if (methodName == null || !componentWithDeclarationList.isPublic()) {
      return null;
    }
    final List<DartComponent> filteredSubItems = ContainerUtil.filter(subItems, new consulo.util.lang.function.Condition<DartComponent>() {
      @Override
      public boolean value(DartComponent component) {
        return methodName.equals(component.getName());
      }
    });
    if (filteredSubItems.isEmpty() || componentName == null) {
      return null;
    }
    return new LineMarkerInfo<PsiElement>(componentName, componentName.getTextRange(), isInterface ? AllIcons.Gutter.ImplementedMethod :
      AllIcons.Gutter.OverridenMethod, Pass.UPDATE_ALL,
                                          element -> isInterface ? DaemonBundle.message("method.is.implemented.too.many") : DaemonBundle.message("method.is.overridden.too.many"), new GutterIconNavigationHandler<PsiElement>() {
      @Override
      public void navigate(MouseEvent e, PsiElement elt) {
        PsiElementListNavigator.openTargets(e,
                                            DartResolveUtil.getComponentNames(filteredSubItems).toArray(new
                                                                                                          NavigatablePsiElement[filteredSubItems
                                              .size()]),
                                            isInterface ? DaemonBundle.message("navigation.title.implementation" +
                                                                                 ".method",
                                                                               componentWithDeclarationList.getName(),
                                                                               filteredSubItems.size()) : DaemonBundle.message(
                                              "navigation.title" +
                                                ".overrider.method",
                                              componentWithDeclarationList.getName(),
                                              filteredSubItems.size()),
                                            "Implementations of " + componentWithDeclarationList.getName(),
                                            new DefaultPsiElementCellRenderer());
      }
    }, GutterIconRenderer.Alignment.RIGHT
    );
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return DartLanguage.INSTANCE;
  }
}
