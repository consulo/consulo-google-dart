package com.jetbrains.lang.dart.ide.actions;

import com.jetbrains.lang.dart.DartIcons;
import com.jetbrains.lang.dart.util.DartFileTemplateUtil;
import consulo.fileTemplate.FileTemplate;
import consulo.fileTemplate.FileTemplateManager;
import consulo.fileTemplate.FileTemplateUtil;
import consulo.google.dart.localize.DartLocalize;
import consulo.ide.action.CreateFileFromTemplateDialog;
import consulo.ide.action.CreateFromTemplateAction;
import consulo.language.psi.PsiDirectory;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.util.IncorrectOperationException;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.ui.ex.InputValidatorEx;
import consulo.ui.image.Image;
import consulo.util.lang.StringUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fedor.Korotkov
 */
public class CreateDartFileAction extends CreateFromTemplateAction<PsiFile> {
  public CreateDartFileAction() {
    super(DartLocalize.actionCreateNewFile(), DartLocalize.actionCreateNewFile(), DartIcons.Dart);
  }

  @Override
  protected LocalizeValue getActionName(PsiDirectory directory, String newName, String templateName) {
    return DartLocalize.progressCreatingFile(newName);
  }

  @Override
  protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
    builder.setTitle(LocalizeValue.localizeTODO("Create New Class"));
    for (FileTemplate fileTemplate : DartFileTemplateUtil.getApplicableTemplates()) {
      final String templateName = fileTemplate.getName();
      final String shortName = DartFileTemplateUtil.getTemplateShortName(templateName);
      final Image icon = DartFileTemplateUtil.getTemplateIcon(templateName);
      builder.addKind(LocalizeValue.of(shortName), icon, templateName);
    }
    builder.setValidator(new InputValidatorEx() {
      @Override
      public String getErrorText(String inputString) {
        if (inputString.length() > 0 && !StringUtil.isJavaIdentifier(inputString)) {
          return "This is not a valid Dart name";
        }
        return null;
      }

      @Override
      public boolean checkInput(String inputString) {
        return true;
      }

      @Override
      public boolean canClose(String inputString) {
        return !StringUtil.isEmptyOrSpaces(inputString) && getErrorText(inputString) == null;
      }
    });
  }

  @Nullable
  @Override
  protected PsiFile createFile(String className, String templateName, PsiDirectory dir) {
    try {
      return createFile(className, dir, templateName).getContainingFile();
    }
    catch (Exception e) {
      throw new IncorrectOperationException(e.getMessage(), e);
    }
  }

  private static PsiElement createFile(String className, @Nonnull PsiDirectory directory, final String templateName)
    throws Exception {
    final Map<String, Object> props = new HashMap<>(FileTemplateManager.getInstance(directory.getProject()).getDefaultVariables());
    props.put(FileTemplate.ATTRIBUTE_NAME, className);

    final FileTemplate template = FileTemplateManager.getInstance(directory.getProject()).getInternalTemplate(templateName);

    return FileTemplateUtil.createFromTemplate(template, className, props, directory, CreateDartFileAction.class.getClassLoader());
  }
}
