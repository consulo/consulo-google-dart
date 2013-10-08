package com.jetbrains.lang.dart.ide.actions;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.CapturingProcessHandler;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.ide.module.DartModuleExtension;
import com.jetbrains.lang.dart.ide.settings.DartSdkUtil;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import icons.DartIcons;
import org.consulo.yaml.psi.YAMLFile;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;

/**
 * @author: Fedor.Korotkov
 */
public class DartPubAction extends AnAction {
  private static final Logger LOG = Logger.getInstance("#com.jetbrains.lang.dart.ide.actions.DartPubAction");

  public DartPubAction() {
    super(DartIcons.Dart);
  }

  @Override
  public void update(AnActionEvent e) {
    final DataContext dataContext = e.getDataContext();
    final Presentation presentation = e.getPresentation();

    final PsiFile psiFile = LangDataKeys.PSI_FILE.getData(dataContext);
    final boolean enabled = psiFile instanceof YAMLFile;

    presentation.setVisible(enabled);
    presentation.setEnabled(enabled);

    if (enabled) {
      boolean update = DartResolveUtil.findPackagesFolder(psiFile) != null;
      presentation.setText(update ? DartBundle.message("dart.pub.update") : DartBundle.message("dart.pub.install"));
    }
  }

  @Override
  public void actionPerformed(final AnActionEvent e) {
    final PsiFile psiFile = LangDataKeys.PSI_FILE.getData(e.getDataContext());
    Module module = LangDataKeys.MODULE.getData(e.getDataContext());
    if (!(psiFile instanceof YAMLFile) || module == null) {
      Messages.showOkCancelDialog(e.getProject(), DartBundle.message("dart.sdk.bad.dartpub.file"),
                                  DartBundle.message("dart.warning"),
                                  DartIcons.Dart);
      return;
    }
    final VirtualFile virtualFile = DartResolveUtil.getRealVirtualFile(psiFile);

    if (virtualFile == null) {
      return;
    }

    final boolean update = DartResolveUtil.findPackagesFolder(module) != null;
    final Sdk sdk = ModuleUtilCore.getSdk(module, DartModuleExtension.class);

    final VirtualFile dartPub = DartSdkUtil.getPub(sdk);
    if (dartPub == null) {
      Messages.showOkCancelDialog(e.getProject(),
                                  DartBundle.message("dart.sdk.bad.dartpub.path", sdk == null ? "" : DartSdkUtil.getPubPath(sdk)),
                                  DartBundle.message("dart.warning"),
                                  DartIcons.Dart);
      return;
    }

    new Task.Backgroundable(psiFile.getProject(), "Dart Pub", true) {
      public void run(@NotNull ProgressIndicator indicator) {
        indicator.setText("Running pub manager...");
        indicator.setFraction(0.0);
        final GeneralCommandLine command = new GeneralCommandLine();
        command.setExePath(dartPub.getPath());
        command.setWorkDirectory(virtualFile.getParent().getPath());
        command.addParameter(update ? "update" : "install");

        command.getEnvironment().put("DART_SDK", sdk.getHomePath());

        // save on disk
        ApplicationManager.getApplication().invokeAndWait(new Runnable() {
          @Override
          public void run() {
            FileDocumentManager.getInstance().saveAllDocuments();
          }
        }, ModalityState.defaultModalityState());


        try {
          final ProcessOutput processOutput = new CapturingProcessHandler(
            command.createProcess(),
            Charset.defaultCharset(),
            command.getCommandLineString()
          ).runProcess();

          LOG.debug("pub exited with exit code: " + processOutput.getExitCode());
          LOG.debug(processOutput.getStdout());
          LOG.debug(processOutput.getStderr());

          final String output = processOutput.getStdout().trim();
          final boolean ok = isOK(update, output);

          if (!ok) {
            Notifications.Bus.notify(new Notification(e.getPresentation().getText(),
                                                      DartBundle.message("dart.pub.title"),
                                                      DartBundle.message("dart.pub.error", output, processOutput.getStderr()),
                                                      NotificationType.WARNING));
          }
          else {
            Notifications.Bus.notify(new Notification(e.getPresentation().getText(),
                                                      DartBundle.message("dart.pub.title"),
                                                      output,
                                                      NotificationType.INFORMATION));
            virtualFile.getParent().refresh(true, false);
          }
        }
        catch (ExecutionException ex) {
          LOG.error(ex);
          Notifications.Bus.notify(new Notification(e.getPresentation().getText(),
                                                    DartBundle.message("dart.pub.title"),
                                                    DartBundle.message("dart.pub.exception", ex.getMessage()),
                                                    NotificationType.ERROR));
        }
        indicator.setFraction(1.0);
      }
    }.setCancelText("Stop").queue();
  }

  private static boolean isOK(boolean update, String status) {
    if (update && status.contains("Dependencies updated!")) {
      return true;
    }
    if (!update && status.contains("Dependencies installed!")) {
      return true;
    }
    return false;
  }
}
