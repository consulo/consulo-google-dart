package com.jetbrains.lang.dart.ide.structure;

import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponent;
import consulo.application.AllIcons;
import consulo.fileEditor.structureView.StructureViewModel;
import consulo.fileEditor.structureView.StructureViewTreeElement;
import consulo.fileEditor.structureView.tree.ActionPresentation;
import consulo.fileEditor.structureView.tree.ActionPresentationData;
import consulo.fileEditor.structureView.tree.Filter;
import consulo.fileEditor.structureView.tree.TreeElement;
import consulo.ide.IdeBundle;
import consulo.language.editor.structureView.StructureViewModelBase;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import org.jetbrains.annotations.NonNls;

import jakarta.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
public class DartStructureViewModel extends StructureViewModelBase implements StructureViewModel.ElementInfoProvider {
  public DartStructureViewModel(@Nonnull PsiFile psiFile) {
    super(psiFile, new DartStructureViewElement(psiFile));
    withSuitableClasses(DartComponent.class, DartClass.class);
  }

  @Override
  public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
    return false;
  }

  @Nonnull
  @Override
  public Filter[] getFilters() {
    return new Filter[]{ourFieldsFilter};
  }

  @Override
  public boolean isAlwaysLeaf(StructureViewTreeElement element) {
    final Object value = element.getValue();
    return value instanceof DartComponent && !(value instanceof DartClass);
  }

  @Override
  public boolean shouldEnterElement(Object element) {
    return element instanceof DartClass;
  }


  private static final Filter ourFieldsFilter = new Filter() {
    @NonNls
    public static final String ID = "SHOW_FIELDS";

    @Override
    public boolean isVisible(TreeElement treeNode) {
      if (!(treeNode instanceof DartStructureViewElement)) return true;
      final PsiElement element = ((DartStructureViewElement)treeNode).getRealElement();

      DartComponentType type = DartComponentType.typeOf(element);
      if (type == DartComponentType.FIELD || type == DartComponentType.VARIABLE) {
        return false;
      }

      if (element instanceof DartComponent && (((DartComponent)element).isGetter() || ((DartComponent)element).isGetter())) {
        return false;
      }

      return true;
    }

    @Override
    public boolean isReverted() {
      return true;
    }

    @Override
    @Nonnull
    public ActionPresentation getPresentation() {
      return new ActionPresentationData(
        IdeBundle.message("action.structureview.show.fields"),
        null, AllIcons.Nodes.PpFile
      );
    }

    @Override
    @Nonnull
    public String getName() {
      return ID;
    }
  };
}
