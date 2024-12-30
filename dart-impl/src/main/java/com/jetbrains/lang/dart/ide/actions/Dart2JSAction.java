package com.jetbrains.lang.dart.ide.actions;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartIcons;
import com.jetbrains.lang.dart.ide.actions.ui.Dart2JSSettingsDialog;
import com.jetbrains.lang.dart.ide.settings.DartSdkUtil;
import com.jetbrains.lang.dart.psi.DartFile;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import consulo.application.Application;
import consulo.application.ApplicationManager;
import consulo.application.progress.ProgressIndicator;
import consulo.application.progress.ProgressManager;
import consulo.application.progress.Task;
import consulo.content.bundle.Sdk;
import consulo.dart.DartNotificationGroup;
import consulo.dart.module.extension.DartModuleExtension;
import consulo.document.FileDocumentManager;
import consulo.language.editor.LangDataKeys;
import consulo.language.psi.PsiFile;
import consulo.language.util.ModuleUtilCore;
import consulo.localize.LocalizeValue;
import consulo.logging.Logger;
import consulo.module.Module;
import consulo.process.ExecutionException;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.local.ScriptRunnerUtil;
import consulo.project.Project;
import consulo.project.ui.notification.Notification;
import consulo.project.ui.notification.NotificationType;
import consulo.project.ui.notification.Notifications;
import consulo.ui.ex.action.AnAction;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.action.Presentation;
import consulo.ui.ex.awt.Messages;
import consulo.util.io.FileUtil;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.VirtualFileManager;

import jakarta.annotation.Nonnull;
import java.io.File;

/**
 * @author: Fedor.Korotkov
 */
public class Dart2JSAction extends AnAction {
  private static final Logger LOG = Logger.getInstance(Dart2JSAction.class);

  public Dart2JSAction() {
    super(DartIcons.Dart);
  }

  @Override
  public void update(AnActionEvent e) {
    final Presentation presentation = e.getPresentation();

    final boolean enabled = e.getData(LangDataKeys.PSI_FILE) instanceof DartFile;

    presentation.setVisible(enabled);
    presentation.setEnabled(enabled);
  }

  @Override
  public void actionPerformed(final AnActionEvent e) {
    final PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
    if (!(psiFile instanceof DartFile)) {
      return;
    }
    final VirtualFile virtualFile = DartResolveUtil.getRealVirtualFile(psiFile);
    if (virtualFile == null) {
      return;
    }

    Module module = ModuleUtilCore.findModuleForPsiElement(psiFile);
    if (module == null) {
      return;
    }

    final Sdk sdk = ModuleUtilCore.getSdk(module, DartModuleExtension.class);
    final VirtualFile dart2js = DartSdkUtil.getDart2JS(sdk);
    if (dart2js == null) {
      Messages.showOkCancelDialog(e.getData(Project.KEY), DartBundle.message("dart.sdk.bad.dart2js.path", DartSdkUtil.getDart2JSPath(sdk)),
                                  DartBundle.message("dart.warning"),
                                  DartIcons.Dart);
      return;
    }

    final String jsFilePath = virtualFile.getPath() + ".js";
    final Dart2JSSettingsDialog dialog = new Dart2JSSettingsDialog(psiFile.getProject(), jsFilePath);
    dialog.show();
    if (!dialog.isOK()) {
      return;
    }

    new Task.Backgroundable(psiFile.getProject(), "dart2js", true) {
      public void run(@Nonnull ProgressIndicator indicator) {
        indicator.setText("Running dart2js...");
        indicator.setFraction(0.0);
        final GeneralCommandLine command = new GeneralCommandLine();
        command.setExePath(dart2js.getPath());
        if (dialog.isCheckedMode()) {
          command.addParameter("--checked");
        }
        if (dialog.isMinify()) {
          command.addParameter("--minify");
        }
        command.addParameter("--out=" + dialog.getOutputPath());
        command.addParameter(virtualFile.getPath());

        // save on disk
        ApplicationManager.getApplication().invokeAndWait(new Runnable() {
          @Override
          public void run() {
            FileDocumentManager.getInstance().saveAllDocuments();
          }
        }, Application.get().getDefaultModalityState());

        try {
          final String output = ScriptRunnerUtil.getProcessOutput(command);
          ProgressManager.progress("");
          LOG.debug(output);
          boolean error = !output.isEmpty();
          if (error) {
            Notifications.Bus.notify(new Notification(DartNotificationGroup.DART2JS,
                                                      DartBundle.message("dart2js.title"),
                                                      DartBundle.message("dart2js.js.file.creation.error", output),
                                                      NotificationType.ERROR));
            return;
          }
          Notifications.Bus.notify(new Notification(DartNotificationGroup.DART2JS,
                                                    DartBundle.message("dart2js.title"),
                                                    DartBundle.message("dart2js.js.file.created", jsFilePath),
                                                    NotificationType.INFORMATION));

          final File parentDir = FileUtil.getParentFile(new File(dialog.getOutputPath()));
          assert parentDir != null;
          final VirtualFile outputParentVirtualFile = VirtualFileManager.getInstance().findFileByNioPath(parentDir.toPath());
          if (outputParentVirtualFile != null) {
            outputParentVirtualFile.refresh(true, false);
          }
        }
        catch (ExecutionException ex) {
          LOG.error(ex);
          Notifications.Bus.notify(new Notification(DartNotificationGroup.DART2JS,
                                                    DartBundle.message("dart2js.title"),
                                                    DartBundle.message("dart2js.js.file.creation.error", ex.getMessage()),
                                                    NotificationType.ERROR));
        }
        indicator.setFraction(1.0);
      }
    }.setCancelText(LocalizeValue.localizeTODO("Stop")).queue();
  }
}
