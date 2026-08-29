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
import consulo.language.editor.localize.DaemonLocalize;
import consulo.language.editor.ui.navigation.PsiTargetNavigationService;
import consulo.language.psi.PsiElement;
import consulo.localize.LocalizeValue;
import consulo.ui.event.ComponentEvent;
import consulo.util.collection.ContainerUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ExtensionImpl
public class DartImplementationsMarkerProvider implements LineMarkerProvider {
    private final PsiTargetNavigationService myPsiTargetNavigationService;

    @Inject
    public DartImplementationsMarkerProvider(PsiTargetNavigationService psiTargetNavigationService) {
        myPsiTargetNavigationService = psiTargetNavigationService;
    }

    @Override
    public LineMarkerInfo getLineMarkerInfo(@Nonnull PsiElement element) {
        return null;
    }

    @Override
    public void collectSlowLineMarkers(@Nonnull List<PsiElement> elements, @Nonnull Collection<LineMarkerInfo> result) {
        elements = ContainerUtil.filter(elements, element -> element instanceof DartClass);
        if (elements.size() > 20) {
            return;
        }
        for (PsiElement dartClass : elements) {
            collectMarkers(result, (DartClass) dartClass);
        }
    }

    private void collectMarkers(Collection<LineMarkerInfo> result, DartClass dartClass) {
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

    private LineMarkerInfo createImplementationMarker(final DartClass dartClass, final List<DartClass> items) {
        final DartComponentName componentName = dartClass.getComponentName();
        return new LineMarkerInfo<PsiElement>(componentName, componentName.getTextRange(), AllIcons.Gutter.OverridenMethod, Pass.UPDATE_ALL,
            element -> DaemonBundle.message("method.is.implemented.too.many"), (e, elt) -> {
            List<DartComponentName> targets = DartResolveUtil.getComponentNames(items);

            myPsiTargetNavigationService
                .newNavigator(() -> targets)
                .title(DaemonLocalize.navigationTitleSubclass(dartClass.getName(), items.size(), ""))
                .findUsagesTitle(LocalizeValue.localizeTODO("Superclasses of " + dartClass.getName()))
                .navigate(e, dartClass.getProject());
        }, GutterIconRenderer.Alignment.RIGHT
        );
    }

    @Nullable
    private LineMarkerInfo tryCreateImplementationMarker(final DartComponent componentWithDeclarationList,
                                                         List<DartComponent> subItems,
                                                         final boolean isInterface) {
        final PsiElement componentName = componentWithDeclarationList.getComponentName();
        final String methodName = componentWithDeclarationList.getName();
        if (methodName == null || !componentWithDeclarationList.isPublic()) {
            return null;
        }
        final List<DartComponent> filteredSubItems = ContainerUtil.filter(subItems, component -> methodName.equals(component.getName()));
        if (filteredSubItems.isEmpty() || componentName == null) {
            return null;
        }
        return new LineMarkerInfo<PsiElement>(componentName, componentName.getTextRange(), isInterface ? AllIcons.Gutter.ImplementedMethod :
            AllIcons.Gutter.OverridenMethod, Pass.UPDATE_ALL,
            element -> isInterface ? DaemonBundle.message("method.is.implemented.too.many") : DaemonBundle.message("method.is.overridden.too.many"), new GutterIconNavigationHandler<PsiElement>() {
            @Override
            public void navigate(ComponentEvent<?> e, PsiElement elt) {
                List<DartComponentName> targets = DartResolveUtil.getComponentNames(filteredSubItems);

                LocalizeValue title = isInterface
                    ? DaemonLocalize.navigationTitleImplementationMethod(componentWithDeclarationList.getName(), filteredSubItems.size())
                    : DaemonLocalize.navigationTitleOverriderMethod(componentWithDeclarationList.getName(), filteredSubItems.size());

                myPsiTargetNavigationService
                    .newNavigator(() -> targets)
                    .title(title)
                    .findUsagesTitle(LocalizeValue.localizeTODO("Implementations of " + componentWithDeclarationList.getName()))
                    .navigate(e, componentWithDeclarationList.getProject());
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
