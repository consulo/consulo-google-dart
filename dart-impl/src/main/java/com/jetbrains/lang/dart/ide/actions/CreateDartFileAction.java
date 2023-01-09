package com.jetbrains.lang.dart.ide.actions;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartIcons;
import com.jetbrains.lang.dart.util.DartFileTemplateUtil;
import consulo.fileTemplate.FileTemplate;
import consulo.fileTemplate.FileTemplateManager;
import consulo.fileTemplate.FileTemplateUtil;
import consulo.ide.IdeBundle;
import consulo.ide.action.CreateFileFromTemplateDialog;
import consulo.ide.action.CreateFromTemplateAction;
import consulo.language.psi.PsiDirectory;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.util.IncorrectOperationException;
import consulo.project.Project;
import consulo.ui.ex.InputValidatorEx;
import consulo.ui.image.Image;
import consulo.util.lang.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Properties;

/**
 * @author: Fedor.Korotkov
 */
public class CreateDartFileAction extends CreateFromTemplateAction<PsiFile> {
  public CreateDartFileAction() {
    super(DartBundle.message("action.create.new.file"), DartBundle.message("action.create.new.file"), DartIcons.Dart);
  }

  @Override
  protected String getActionName(PsiDirectory directory, String newName, String templateName) {
    return DartBundle.message("progress.creating.file", newName);
  }

  @Override
  protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
    builder.setTitle(IdeBundle.message("action.create.new.class"));
    for (FileTemplate fileTemplate : DartFileTemplateUtil.getApplicableTemplates()) {
      final String templateName = fileTemplate.getName();
      final String shortName = DartFileTemplateUtil.getTemplateShortName(templateName);
      final Image icon = DartFileTemplateUtil.getTemplateIcon(templateName);
      builder.addKind(shortName, icon, templateName);
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
    final Properties props = new Properties(FileTemplateManager.getInstance().getDefaultProperties(directory.getProject()));
    props.setProperty(FileTemplate.ATTRIBUTE_NAME, className);

    final FileTemplate template = FileTemplateManager.getInstance().getInternalTemplate(templateName);

    return FileTemplateUtil.createFromTemplate(template, className, props, directory, CreateDartFileAction.class.getClassLoader());
  }
}
