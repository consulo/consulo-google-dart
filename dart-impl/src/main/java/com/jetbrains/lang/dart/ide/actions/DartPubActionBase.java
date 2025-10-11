package com.jetbrains.lang.dart.ide.actions;

import com.jetbrains.lang.dart.DartIcons;
import com.jetbrains.lang.dart.DartProjectComponent;
import consulo.application.Application;
import consulo.application.ApplicationManager;
import consulo.application.progress.ProgressIndicator;
import consulo.application.progress.Task;
import consulo.application.util.SystemInfo;
import consulo.content.bundle.Sdk;
import consulo.dart.DartNotificationGroup;
import consulo.dart.module.extension.DartModuleExtension;
import consulo.document.FileDocumentManager;
import consulo.google.dart.localize.DartLocalize;
import consulo.ide.setting.ShowSettingsUtil;
import consulo.language.editor.CommonDataKeys;
import consulo.language.editor.LangDataKeys;
import consulo.language.psi.PsiFile;
import consulo.language.util.ModuleUtilCore;
import consulo.localize.LocalizeValue;
import consulo.logging.Logger;
import consulo.module.Module;
import consulo.process.ExecutionException;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.util.CapturingProcessUtil;
import consulo.process.util.ProcessOutput;
import consulo.project.ui.notification.Notification;
import consulo.project.ui.notification.NotificationType;
import consulo.project.ui.notification.Notifications;
import consulo.ui.ex.action.AnAction;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.awt.Messages;
import consulo.util.lang.Pair;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.File;

abstract public class DartPubActionBase extends AnAction {
    private static final Logger LOG = Logger.getInstance(DartPubActionBase.class);

    public DartPubActionBase() {
        super(DartIcons.Dart);
    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setTextValue(getPresentableText());
        final boolean enabled = getModuleAndPubspecYamlFile(e) != null;
        e.getPresentation().setVisible(enabled);
        e.getPresentation().setEnabled(enabled);
    }

    @Nullable
    private static Pair<Module, VirtualFile> getModuleAndPubspecYamlFile(final AnActionEvent e) {
        final Module module = e.getData(LangDataKeys.MODULE);
        final PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        if (module != null && psiFile != null && psiFile.getName().equalsIgnoreCase("pubspec.yaml")) {
            final VirtualFile file = psiFile.getOriginalFile().getVirtualFile();
            return file != null ? Pair.create(module, file) : null;
        }
        return null;
    }

    protected abstract LocalizeValue getPresentableText();

    protected abstract String getPubCommand();

    protected abstract LocalizeValue getSuccessMessage();

    @Override
    public void actionPerformed(final AnActionEvent e) {
        final Pair<Module, VirtualFile> moduleAndPubspecYamlFile = getModuleAndPubspecYamlFile(e);
        if (moduleAndPubspecYamlFile == null) {
            return;
        }

        File sdkRoot = getSdkRoot(moduleAndPubspecYamlFile);
        if (sdkRoot == null) {
            final int answer =
                Messages.showDialog(moduleAndPubspecYamlFile.first.getProject(), "Dart SDK is not configured", getPresentableText().get(), new String[]{
                    "Configure SDK",
                    "Cancel"
                }, 0, Messages.getErrorIcon());
            if (answer != 0) {
                return;
            }

            ShowSettingsUtil.getInstance().showSettingsDialog(moduleAndPubspecYamlFile.first.getProject(), DartLocalize.dartTitle().get());

            sdkRoot = getSdkRoot(moduleAndPubspecYamlFile);
            if (sdkRoot == null) {
                return;
            }
        }

        File pubFile = new File(sdkRoot, SystemInfo.isWindows ? "bin/pub.bat" : "bin/pub");
        if (!pubFile.isFile()) {
            Messages.showInfoMessage(moduleAndPubspecYamlFile.first.getProject(),
                DartLocalize.dartSdkBadDartpubPath(pubFile.getPath()).get(),
                getPresentableText().get());

            return;
        }

        doExecute(moduleAndPubspecYamlFile.first, moduleAndPubspecYamlFile.second, sdkRoot.getPath(), pubFile.getPath());
    }

    private void doExecute(final Module module, final VirtualFile pubspecYamlFile, final String sdkPath, final String pubPath) {
        final Task.Backgroundable task = new Task.Backgroundable(module.getProject(), getPresentableText(), true) {
            public void run(@Nonnull ProgressIndicator indicator) {
                indicator.setText(DartLocalize.dartPub0InProgress(getPubCommand()).get());
                indicator.setIndeterminate(true);
                final GeneralCommandLine command = new GeneralCommandLine();
                command.setExePath(pubPath);
                command.setWorkDirectory(pubspecYamlFile.getParent().getPath());
                command.addParameter(getPubCommand());
                command.getEnvironment().put("DART_SDK", sdkPath);

                ApplicationManager.getApplication().invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        FileDocumentManager.getInstance().saveAllDocuments();
                    }
                }, Application.get().getDefaultModalityState());


                try {
                    final ProcessOutput processOutput = CapturingProcessUtil.execAndGetOutput(command);
                    final String err = processOutput.getStderr().trim();

                    LOG.debug("pub " + getPubCommand() + ", exit code: " + processOutput.getExitCode() + ", err:\n" +
                        err + "\nout:\n" + processOutput.getStdout());

                    if (err.isEmpty()) {
                        Notifications.Bus.notify(new Notification(DartNotificationGroup.DART_PUB_TOOL,
                            getPresentableText().get(),
                            getSuccessMessage().get(),
                            NotificationType.INFORMATION));
                    }
                    else {
                        Notifications.Bus.notify(new Notification(DartNotificationGroup.DART_PUB_TOOL,
                            getPresentableText().get(),
                            err,
                            NotificationType.ERROR));
                    }

                    ApplicationManager.getApplication().invokeLater(new Runnable() {
                        public void run() {
                            DartProjectComponent.excludePackagesFolders(module, pubspecYamlFile);
                        }
                    });
                }
                catch (ExecutionException ex) {
                    LOG.error(ex);
                    Notifications.Bus.notify(new Notification(DartNotificationGroup.DART_PUB_TOOL,
                        getPresentableText().get(),
                        DartLocalize.dartPubException(ex.getMessage()).get(),
                        NotificationType.ERROR));
                }
            }
        };

        task.queue();
    }

    @Nullable
    private static File getSdkRoot(final Pair<Module, VirtualFile> moduleAndPubspecYamlFile) {
        final Sdk sdk = ModuleUtilCore.getSdk(moduleAndPubspecYamlFile.first, DartModuleExtension.class);
        final String sdkPath = sdk == null ? null : sdk.getHomePath();
        final File sdkRoot = sdkPath == null || StringUtil.isEmptyOrSpaces(sdkPath) ? null : new File(sdkPath);
        return sdkRoot == null || !sdkRoot.isDirectory() ? null : sdkRoot;
    }
}
