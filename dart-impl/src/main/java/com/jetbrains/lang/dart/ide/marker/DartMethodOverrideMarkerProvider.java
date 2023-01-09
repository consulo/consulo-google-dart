package com.jetbrains.lang.dart.ide.marker;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.DartLanguage;
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
import consulo.language.psi.util.PsiTreeUtil;
import consulo.ui.image.Image;
import consulo.util.collection.ContainerUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@ExtensionImpl
public class DartMethodOverrideMarkerProvider implements LineMarkerProvider {

  @Override
  public LineMarkerInfo getLineMarkerInfo(@Nonnull PsiElement element) {
    if (!(element instanceof DartComponentName)) {
      return null;
    }
    final PsiElement parent = element.getParent();
    if (DartComponentType.typeOf(parent) == DartComponentType.METHOD) {
      final DartClass dartClass = PsiTreeUtil.getParentOfType(element, DartClass.class);
      return dartClass == null ? null : createOverrideMarker(dartClass, (DartComponent)parent);
    }
    return null;
  }

  @Override
  public void collectSlowLineMarkers(@Nonnull List<PsiElement> elements, @Nonnull Collection<LineMarkerInfo> result) {
  }

  @Nullable
  private static LineMarkerInfo createOverrideMarker(DartClass dartClass, DartComponent dartComponent) {
    return tryCreateOverrideMarker(dartComponent, DartResolveUtil.findNamedSuperComponents(dartClass));
  }

  @Nullable
  private static LineMarkerInfo tryCreateOverrideMarker(final DartComponent methodDeclaration, List<DartComponent> superItems) {
    final String methodName = methodDeclaration.getName();
    if (methodName == null || !methodDeclaration.isPublic()) {
      return null;
    }
    final List<DartComponent> filteredSuperItems = ContainerUtil.filter(superItems, component -> methodName.equals(component.getName()));
    if (filteredSuperItems.isEmpty()) {
      return null;
    }
    final PsiElement element = methodDeclaration.getComponentName();
    final DartComponent dartComponent = filteredSuperItems.iterator().next();
    final boolean overrides = !dartComponent.isAbstract();
    Image icon = overrides ? AllIcons.Gutter.OverridingMethod : AllIcons.Gutter.ImplementingMethod;
    assert element != null;
    return new LineMarkerInfo<PsiElement>(element, element.getTextRange(), icon, Pass.UPDATE_ALL, new Function<PsiElement, String>() {
      @Override
      public String apply(PsiElement element) {
        final DartClass superDartClass = PsiTreeUtil.getParentOfType(methodDeclaration, DartClass.class);
        if (superDartClass == null) {
          return "null";
        }
        if (overrides) {
          return DartBundle.message("overrides.method.in", methodDeclaration.getName(), superDartClass.getName());
        }
        return DartBundle.message("implements.method.in", methodDeclaration.getName(), superDartClass.getName());
      }
    }, new GutterIconNavigationHandler<PsiElement>() {
      @Override
      public void navigate(MouseEvent e, PsiElement elt) {
        PsiElementListNavigator.openTargets(e,
                                            DartResolveUtil.getComponentNames(filteredSuperItems).toArray(new
                                                                                                            NavigatablePsiElement[filteredSuperItems
                                              .size()]),
                                            DaemonBundle.message("navigation.title.super.method",
                                                                 methodDeclaration.getName()),
                                            DaemonBundle.message("navigation.findUsages.title.super.method", methodDeclaration.getName()),
                                            new DefaultPsiElementCellRenderer());
      }
    }, GutterIconRenderer.Alignment.LEFT
    );
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return DartLanguage.INSTANCE;
  }
}
