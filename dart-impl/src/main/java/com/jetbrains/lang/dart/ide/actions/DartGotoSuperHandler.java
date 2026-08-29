package com.jetbrains.lang.dart.ide.actions;

import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponent;
import com.jetbrains.lang.dart.util.DartClassResolveResult;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.Application;
import consulo.codeEditor.Editor;
import consulo.language.Language;
import consulo.language.editor.action.GotoSuperActionHander;
import consulo.language.editor.localize.DaemonLocalize;
import consulo.language.editor.ui.navigation.PsiTargetNavigationService;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.util.collection.ContainerUtil;
import consulo.util.lang.function.Condition;
import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fedor.Korotkov
 */
@ExtensionImpl
public class DartGotoSuperHandler implements GotoSuperActionHander {
    private final PsiTargetNavigationService myPsiTargetNavigationService;

    @Inject
    public DartGotoSuperHandler(PsiTargetNavigationService psiTargetNavigationService) {
        myPsiTargetNavigationService = psiTargetNavigationService;
    }

    @Override
    public boolean isValidFor(Editor editor, PsiFile file) {
        return file.getLanguage() == DartLanguage.INSTANCE;
    }

    @RequiredUIAccess
    @Override
    public void invoke(@Nonnull Project project, @Nonnull Editor editor, @Nonnull PsiFile file) {
        final PsiElement at = file.findElementAt(editor.getCaretModel().getOffset());

        final DartComponent component = PsiTreeUtil.getParentOfType(at, DartComponent.class);
        final DartClass dartClass = PsiTreeUtil.getParentOfType(at, DartClass.class);
        if (at == null || dartClass == null || component == null) {
            return;
        }

        final List<DartClass> supers = new ArrayList<DartClass>();
        final DartClassResolveResult dartClassResolveResult = DartResolveUtil.resolveClassByType(dartClass.getSuperClass());
        if (dartClassResolveResult.getDartClass() != null) {
            supers.add(dartClassResolveResult.getDartClass());
        }
        List<DartClassResolveResult> implementsAndMixinsList = DartResolveUtil.resolveClassesByTypes(
            DartResolveUtil.getImplementsAndMixinsList(dartClass)
        );
        for (DartClassResolveResult resolveResult : implementsAndMixinsList) {
            final DartClass resolveResultDartClass = resolveResult.getDartClass();
            if (resolveResultDartClass != null) {
                supers.add(resolveResultDartClass);
            }
        }
        final List<DartComponent> superItems = DartResolveUtil.findNamedSubComponents(false, supers.toArray(new DartClass[supers.size()]));

        final DartComponentType type = DartComponentType.typeOf(component);
        if (type == DartComponentType.METHOD) {
            tryNavigateToSuperMethod(editor, component, superItems);
        }
        else if (!supers.isEmpty() && component instanceof DartClass) {
            myPsiTargetNavigationService
                .newNavigator(() -> DartResolveUtil.getComponentNames(supers))
                .title(DaemonLocalize.navigationTitleSubclass(component.getName(), supers.size(), ""))
                .findUsagesTitle(LocalizeValue.localizeTODO("Subclasses of " + component.getName()))
                .navigate(editor, project);
        }
    }

    private static void tryNavigateToSuperMethod(Editor editor,
                                                 DartComponent methodDeclaration,
                                                 List<DartComponent> superItems) {
        final String methodName = methodDeclaration.getName();
        if (methodName == null || !methodDeclaration.isPublic()) {
            return;
        }
        final List<DartComponent> filteredSuperItems = ContainerUtil.filter(superItems, new Condition<DartComponent>() {
            @Override
            public boolean value(DartComponent component) {
                return methodName.equals(component.getName());
            }
        });
        if (!filteredSuperItems.isEmpty()) {
            Application.get().getInstance(PsiTargetNavigationService.class)
                .newNavigator(() -> DartResolveUtil.getComponentNames(filteredSuperItems))
                .title(DaemonLocalize.navigationTitleSuperMethod(methodName))
                .navigate(editor, methodDeclaration.getProject());
        }
    }

    @Override
    public boolean startInWriteAction() {
        return true;
    }

    @Nonnull
    @Override
    public Language getLanguage() {
        return DartLanguage.INSTANCE;
    }
}
