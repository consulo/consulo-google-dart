package com.jetbrains.lang.dart.ide.inspections.analyzer;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.GlobalInspectionContextExtension;
import consulo.language.editor.inspection.HTMLComposer;
import consulo.language.editor.inspection.HTMLComposerExtension;
import consulo.language.editor.inspection.InspectionExtensionsFactory;
import consulo.language.editor.inspection.reference.RefManager;
import consulo.language.editor.inspection.reference.RefManagerExtension;
import consulo.language.psi.PsiElement;
import consulo.project.Project;

@ExtensionImpl(id = "dartGlobalInspection")
public class DartInspectionExtensionsFactory extends InspectionExtensionsFactory {
  @Override
  public GlobalInspectionContextExtension createGlobalInspectionContextExtension() {
    return new DartGlobalInspectionContext();
  }

  @Override
  public RefManagerExtension createRefManagerExtension(RefManager refManager) {
    return null;
  }

  @Override
  public HTMLComposerExtension createHTMLComposerExtension(HTMLComposer composer) {
    return null;
  }

  @Override
  public boolean isToCheckMember(PsiElement element, String id) {
    return true;
  }

  @Override
  public String getSuppressedInspectionIdsIn(PsiElement element) {
    return null;
  }

  @Override
  public boolean isProjectConfiguredToRunInspections(Project project, boolean online) {
    return true;
  }
}
