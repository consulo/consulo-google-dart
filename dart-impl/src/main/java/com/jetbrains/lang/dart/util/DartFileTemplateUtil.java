package com.jetbrains.lang.dart.util;

import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.DartIcons;
import consulo.application.AllIcons;
import consulo.fileTemplate.FileTemplate;
import consulo.fileTemplate.FileTemplateManager;
import consulo.ui.image.Image;
import consulo.ui.image.ImageEffects;
import consulo.util.collection.ContainerUtil;
import consulo.util.collection.SmartList;
import consulo.util.lang.function.Condition;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author: Fedor.Korotkov
 */
public class DartFileTemplateUtil {
  private final static String DART_TEMPLATE_PREFIX = "Dart ";

  public static List<FileTemplate> getApplicableTemplates() {
    return getApplicableTemplates(new Condition<FileTemplate>() {
      @Override
      public boolean value(FileTemplate fileTemplate) {
        return DartFileType.DEFAULT_EXTENSION.equals(fileTemplate.getExtension());
      }
    });
  }

  public static List<FileTemplate> getApplicableTemplates(Condition<FileTemplate> filter) {
    final List<FileTemplate> applicableTemplates = new SmartList<FileTemplate>();
    applicableTemplates.addAll(ContainerUtil.findAll(FileTemplateManager.getInstance().getInternalTemplates(), filter));
    applicableTemplates.addAll(ContainerUtil.findAll(FileTemplateManager.getInstance().getAllTemplates(), filter));
    return applicableTemplates;
  }

  public static String getTemplateShortName(String templateName) {
    if (templateName.startsWith(DART_TEMPLATE_PREFIX)) {
      return templateName.substring(DART_TEMPLATE_PREFIX.length());
    }
    return templateName;
  }

  @Nonnull
  public static Image getTemplateIcon(String name) {
    name = getTemplateShortName(name);
    if ("Class".equals(name)) {
      return ImageEffects.layered(AllIcons.Nodes.Class);
    }
    else if ("Interface".equals(name)) {
      return ImageEffects.layered(AllIcons.Nodes.Interface);
    }
    return DartIcons.Dart;
  }
}
