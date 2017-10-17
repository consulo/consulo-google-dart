package com.jetbrains.lang.dart.ide.actions;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.ScriptRunnerUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiFile;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.ide.actions.ui.Dart2JSSettingsDialog;
import consulo.dart.module.extension.DartModuleExtension;
import com.jetbrains.lang.dart.ide.settings.DartSdkUtil;
import com.jetbrains.lang.dart.psi.DartFile;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author: Fedor.Korotkov
 */
public class Dart2JSAction extends AnAction {
  private static final Logger LOG = Logger.getInstance("#com.jetbrains.lang.dart.ide.actions.Dart2JSAction");

  public Dart2JSAction() {
    super(icons.DartIcons.Dart);
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

    Module module = ModuleUtil.findModuleForPsiElement(psiFile);
    if(module == null) {
      return;
    }

    final Sdk sdk = ModuleUtilCore.getSdk(module, DartModuleExtension.class);
    final VirtualFile dart2js = DartSdkUtil.getDart2JS(sdk);
    if (dart2js == null) {
      Messages.showOkCancelDialog(e.getProject(), DartBundle.message("dart.sdk.bad.dart2js.path", DartSdkUtil.getDart2JSPath(sdk)),
                                  DartBundle.message("dart.warning"),
                                  icons.DartIcons.Dart);
      return;
    }

    final String jsFilePath = virtualFile.getPath() + ".js";
    final Dart2JSSettingsDialog dialog = new Dart2JSSettingsDialog(psiFile.getProject(), jsFilePath);
    dialog.show();
    if (!dialog.isOK()) {
      return;
    }

    new Task.Backgroundable(psiFile.getProject(), "dart2js", true) {
      public void run(@NotNull ProgressIndicator indicator) {
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
        }, ModalityState.defaultModalityState());

        try {
          final String output = ScriptRunnerUtil.getProcessOutput(command);
          ProgressManager.progress("");
          LOG.debug(output);
          boolean error = !output.isEmpty();
          if (error) {
            Notifications.Bus.notify(new Notification(e.getPresentation().getText(),
                                                      DartBundle.message("dart2js.title"),
                                                      DartBundle.message("dart2js.js.file.creation.error", output),
                                                      NotificationType.ERROR));
            return;
          }
          Notifications.Bus.notify(new Notification(e.getPresentation().getText(),
                                                    DartBundle.message("dart2js.title"),
                                                    DartBundle.message("dart2js.js.file.created", jsFilePath),
                                                    NotificationType.INFORMATION));

          final String parentDir = VfsUtil.getParentDir(dialog.getOutputPath());
          assert parentDir != null;
          final VirtualFile outputParentVirtualFile = VirtualFileManager.getInstance().findFileByUrl(VfsUtilCore.pathToUrl(parentDir));
          if (outputParentVirtualFile != null) {
            outputParentVirtualFile.refresh(true, false);
          }
        }
        catch (ExecutionException ex) {
          LOG.error(ex);
          Notifications.Bus.notify(new Notification(e.getPresentation().getText(),
                                                    DartBundle.message("dart2js.title"),
                                                    DartBundle.message("dart2js.js.file.creation.error", ex.getMessage()),
                                                    NotificationType.ERROR));
        }
        indicator.setFraction(1.0);
      }
    }.setCancelText("Stop").queue();
  }
}
