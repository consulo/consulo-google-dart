package com.jetbrains.lang.dart.ide.generation;

import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.ide.DartNamedElementNode;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartClassDefinition;
import com.jetbrains.lang.dart.psi.DartComponent;
import com.jetbrains.lang.dart.psi.DartFile;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import consulo.application.ApplicationManager;
import consulo.codeEditor.Editor;
import consulo.language.Language;
import consulo.language.editor.FileModificationService;
import consulo.language.editor.action.LanguageCodeInsightActionHandler;
import consulo.language.editor.generation.ClassMember;
import consulo.language.editor.generation.MemberChooserBuilder;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.IncorrectOperationException;
import consulo.localize.LocalizeValue;
import consulo.logging.Logger;
import consulo.project.Project;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.undoRedo.CommandProcessor;
import consulo.util.collection.ContainerUtil;
import consulo.util.lang.Pair;
import consulo.util.lang.function.Condition;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author: Fedor.Korotkov
 */
public abstract class BaseDartGenerateHandler implements LanguageCodeInsightActionHandler {
  @Override
  public boolean isValidFor(Editor editor, PsiFile file) {
    return file instanceof DartFile;
  }

  @Override
  public void invoke(@Nonnull Project project, @Nonnull Editor editor, @Nonnull PsiFile file) {
    invoke(project, editor, file, editor.getCaretModel().getOffset());
  }

  @Nonnull
  @Override
  public Language getLanguage() {
    return DartLanguage.INSTANCE;
  }

  @RequiredUIAccess
  public void invoke(Project project, Editor editor, PsiFile file, int offset) {
    if (!FileModificationService.getInstance().prepareFileForWrite(file)) return;
    final DartClass dartClass =
      PsiTreeUtil.getParentOfType(file.findElementAt(offset), DartClassDefinition.class);
    if (dartClass == null) return;

    final List<DartComponent> candidates = new ArrayList<DartComponent>();
    collectCandidates(dartClass, candidates);

    Consumer<Collection<DartNamedElementNode>> acceptor = selectedElements -> {
      final BaseCreateMethodsFix createMethodsFix = createFix(dartClass);
      doInvoke(project, editor, file, selectedElements, createMethodsFix);
    };

    if (!candidates.isEmpty()) {
      final MemberChooserBuilder<DartNamedElementNode> chooser =
        createMemberChooserDialog(project, dartClass, candidates, LocalizeValue.localizeTODO(getTitle()));
      chooser.showAsync(project, data -> {
        List selected = data.getUserData(ClassMember.KEY_OF_LIST);
        if (selected != null) {
          acceptor.accept(selected);
        }
      });
    }
    else {
      acceptor.accept(List.of());
    }
  }

  protected void doInvoke(final Project project,
                          final Editor editor,
                          final PsiFile file,
                          final Collection<DartNamedElementNode> selectedElements,
                          final BaseCreateMethodsFix createMethodsFix) {
    Runnable runnable = new Runnable() {
      public void run() {
        createMethodsFix.addElementsToProcessFrom(selectedElements);
        createMethodsFix.beforeInvoke(project, editor, file);

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
          public void run() {
            try {
              createMethodsFix.invoke(project, editor, file);
            }
            catch (IncorrectOperationException ex) {
              Logger.getInstance(getClass().getName()).error(ex);
            }
          }
        });
      }
    };

    if (CommandProcessor.getInstance().getCurrentCommand() == null) {
      CommandProcessor.getInstance().executeCommand(project, runnable, getClass().getName(), null);
    }
    else {
      runnable.run();
    }
  }

  protected abstract BaseCreateMethodsFix createFix(DartClass haxeClass);

  protected abstract String getTitle();

  protected void collectCandidates(DartClass aClass, List<DartComponent> candidates) {
    final List<DartClass> superClasses = new ArrayList<DartClass>();
    final List<DartClass> superInterfaces = new ArrayList<DartClass>();

    DartResolveUtil.collectSupers(superClasses, superInterfaces, aClass);

    List<DartComponent> classMembers = DartResolveUtil.getNamedSubComponents(aClass);
    List<DartComponent> superClassesMembers = new ArrayList<DartComponent>();
    for (DartClass superClass : superClasses) {
      superClassesMembers.addAll(DartResolveUtil.getNamedSubComponents(superClass));
    }
    List<DartComponent> superInterfacesMembers = new ArrayList<DartComponent>();
    for (DartClass superInterface : superInterfaces) {
      superInterfacesMembers.addAll(DartResolveUtil.getNamedSubComponents(superInterface));
    }

    final Condition<DartComponent> notConstructorCondition = new Condition<DartComponent>() {
      @Override
      public boolean value(DartComponent component) {
        return DartComponentType.typeOf(component) != DartComponentType.CONSTRUCTOR;
      }
    };
    classMembers = ContainerUtil.filter(classMembers, notConstructorCondition);
    superClassesMembers = ContainerUtil.filter(superClassesMembers, notConstructorCondition);
    superInterfacesMembers = ContainerUtil.filter(superInterfacesMembers, notConstructorCondition);

    final Map<Pair<String, Boolean>, DartComponent> classMembersMap = DartResolveUtil.namedComponentToMap(classMembers);
    final Map<Pair<String, Boolean>, DartComponent> superClassesMembersMap = DartResolveUtil.namedComponentToMap(superClassesMembers);
    final Map<Pair<String, Boolean>, DartComponent> superInterfacesMembersMap = DartResolveUtil.namedComponentToMap(superInterfacesMembers);

    collectCandidates(classMembersMap, superClassesMembersMap, superInterfacesMembersMap, candidates);
  }

  protected abstract void collectCandidates(Map<Pair<String, Boolean>, DartComponent> classMembersMap,
                                            Map<Pair<String, Boolean>, DartComponent> superClassesMembersMap,
                                            Map<Pair<String, Boolean>, DartComponent> superInterfacesMembersMap,
                                            List<DartComponent> candidates);

  @Nullable
  protected JComponent getOptionsComponent(DartClass jsClass, final Collection<DartComponent> candidates) {
    return null;
  }

  @Override
  public boolean startInWriteAction() {
    return true;
  }

  protected MemberChooserBuilder<DartNamedElementNode> createMemberChooserDialog(final Project project,
                                                                                 final DartClass dartClass,
                                                                                 final Collection<DartComponent> candidates,
                                                                                 LocalizeValue title) {
    MemberChooserBuilder<DartNamedElementNode> builder = MemberChooserBuilder.create(candidates.toArray(DartNamedElementNode[]::new));
    builder.withTitle(title);
    return builder;
  }
}
