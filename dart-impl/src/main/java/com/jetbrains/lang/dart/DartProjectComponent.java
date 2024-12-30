package com.jetbrains.lang.dart;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.ApplicationManager;
import consulo.application.dumb.DumbAware;
import consulo.content.base.ExcludedContentFolderTypeProvider;
import consulo.dart.module.extension.DartModuleExtension;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.search.FilenameIndex;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.module.content.ModuleRootManager;
import consulo.module.content.ProjectFileIndex;
import consulo.module.content.ProjectRootManager;
import consulo.module.content.layer.ContentEntry;
import consulo.module.content.layer.ModifiableRootModel;
import consulo.module.extension.ModuleExtensionHelper;
import consulo.project.Project;
import consulo.project.startup.PostStartupActivity;
import consulo.ui.UIAccess;
import consulo.util.collection.ContainerUtil;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.util.VirtualFileUtil;
import consulo.virtualFileSystem.util.VirtualFileVisitor;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;

@ExtensionImpl
public class DartProjectComponent implements PostStartupActivity, DumbAware {
  @Override
  public void runActivity(@Nonnull Project project, @Nonnull UIAccess uiAccess) {
    if (!ModuleExtensionHelper.getInstance(project).hasModuleExtension(DartModuleExtension.class)) {
      return;
    }

    project.getApplication().runReadAction(() -> {
      final Collection<VirtualFile> pubspecYamlFiles =
        FilenameIndex.getVirtualFilesByName(project, "pubspec.yaml", GlobalSearchScope.projectScope(project));

      for (VirtualFile pubspecYamlFile : pubspecYamlFiles) {
        final Module module =
          ModuleUtilCore.findModuleForFile(pubspecYamlFile, project);
        if (module != null) {
          excludePackagesFolders(module, pubspecYamlFile);
        }
      }
    });
  }

  public static void excludePackagesFolders(final Module module, final VirtualFile pubspecYamlFile) {
    final VirtualFile root = pubspecYamlFile.getParent();

    root.refresh(true, true, () -> {
      // http://pub.dartlang.org/doc/glossary.html#entrypoint-directory
      // Entrypoint directory: A directory inside your package that is allowed to contain Dart entrypoints.
      // Pub will ensure all of these directories get a “packages” directory, which is needed for “package:” imports to work.
      // Pub has a whitelist of these directories: benchmark, bin, example, test, tool, and web.
      // Any subdirectories of those (except bin) may also contain entrypoints.
      //
      // the same can be seen in the pub tool source code: [repo root]/sdk/lib/_internal/pub/lib/src/entrypoint.dart

      final Collection<VirtualFile> foldersToExclude = new ArrayList<VirtualFile>();
      final ProjectFileIndex fileIndex = ProjectRootManager.getInstance(module.getProject()).getFileIndex();

      final VirtualFile packagesFolder = VirtualFileUtil.findRelativeFile("bin/packages", root);
      if (packagesFolder != null && packagesFolder.isDirectory()) {
        if (fileIndex.isInContent(packagesFolder)) {
          foldersToExclude.add(packagesFolder);
        }
      }

      appendPackagesFolders(foldersToExclude, root.findChild("benchmark"), fileIndex);
      appendPackagesFolders(foldersToExclude, root.findChild("example"), fileIndex);
      appendPackagesFolders(foldersToExclude, root.findChild("test"), fileIndex);
      appendPackagesFolders(foldersToExclude, root.findChild("tool"), fileIndex);
      appendPackagesFolders(foldersToExclude, root.findChild("web"), fileIndex);

      if (!foldersToExclude.isEmpty()) {
        excludeFoldersInWriteAction(module, foldersToExclude);
      }
    });
  }

  private static void appendPackagesFolders(final Collection<VirtualFile> foldersToExclude,
                                            final @Nullable VirtualFile folder,
                                            final ProjectFileIndex fileIndex) {
    if (folder == null) {
      return;
    }

    VirtualFileUtil.visitChildrenRecursively(folder, new VirtualFileVisitor() {
      @Override
      @Nonnull
      public Result visitFileEx(@Nonnull final VirtualFile file) {
        if (file.isDirectory() && "packages".equals(file.getName())) {
          if (fileIndex.isInContent(file)) {
            foldersToExclude.add(file);
          }
          return SKIP_CHILDREN;
        }
        else {
          return CONTINUE;
        }
      }
    });
  }

  private static void excludeFoldersInWriteAction(final Module module, final Collection<VirtualFile> foldersToExclude) {
    final VirtualFile firstItem = ContainerUtil.getFirstItem(foldersToExclude);
    if (firstItem == null) {
      return;
    }

    final VirtualFile contentRoot = ProjectRootManager.getInstance(module.getProject()).getFileIndex().getContentRootForFile(firstItem);
    if (contentRoot == null) {
      return;
    }

    ApplicationManager.getApplication().runWriteAction(new Runnable() {
      @Override
      public void run() {
        final ModifiableRootModel modifiableModel = ModuleRootManager.getInstance(module).getModifiableModel();
        try {
          for (final ContentEntry contentEntry : modifiableModel.getContentEntries()) {
            if (contentEntry.getFile() == contentRoot) {
              for (VirtualFile packagesFolder : foldersToExclude) {
                contentEntry.addFolder(packagesFolder, ExcludedContentFolderTypeProvider.getInstance());
              }
              break;
            }
          }
          modifiableModel.commit();
        }
        catch (Exception e) {
          modifiableModel.dispose();
        }
      }
    });
  }
}
