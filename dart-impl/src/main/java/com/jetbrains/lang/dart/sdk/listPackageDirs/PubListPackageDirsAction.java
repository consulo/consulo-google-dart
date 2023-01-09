package com.jetbrains.lang.dart.sdk.listPackageDirs;

import com.google.dart.engine.sdk.DirectoryBasedDartSdk;
import com.google.dart.engine.source.ExplicitPackageUriResolver;
import consulo.application.ApplicationManager;
import consulo.application.progress.ProgressIndicator;
import consulo.application.progress.ProgressManager;
import consulo.content.base.BinariesOrderRootType;
import consulo.content.bundle.Sdk;
import consulo.content.library.Library;
import consulo.content.library.LibraryTable;
import consulo.content.library.LibraryTablesRegistrar;
import consulo.dart.module.extension.DartModuleExtension;
import consulo.disposer.Disposer;
import consulo.language.editor.LangDataKeys;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.module.content.ModuleRootManager;
import consulo.module.content.layer.ModifiableRootModel;
import consulo.module.content.layer.orderEntry.LibraryOrderEntry;
import consulo.module.content.layer.orderEntry.OrderEntry;
import consulo.project.Project;
import consulo.ui.ex.action.AnAction;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.awt.DialogWrapper;
import consulo.util.io.FileUtil;
import consulo.virtualFileSystem.util.VirtualFileUtil;
import com.jetbrains.lang.dart.DartIcons;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.*;

public class PubListPackageDirsAction extends AnAction {

  public static final String PUB_LIST_PACKAGE_DIRS_LIB_NAME = "Dart pub list-package-dirs";

  public PubListPackageDirsAction() {
    super("Configure Dart package roots using 'pub list-package-dirs'", null, DartIcons.Dart);
  }

  public void update(final AnActionEvent e) {
    e.getPresentation().setEnabledAndVisible(getSdk(e) != null);
  }

  public Sdk getSdk(AnActionEvent e) {
    Module data = e.getData(LangDataKeys.MODULE);
    if (data == null) {
      e.getPresentation().setEnabledAndVisible(false);
      return null;
    }
    return ModuleUtilCore.getSdk(data, DartModuleExtension.class);
  }

  public void actionPerformed(final AnActionEvent e) {
    final Project project = e.getData(Project.KEY);
    if (project == null) {
      return;
    }

    final Sdk sdk = getSdk(e);
    if (sdk == null) {
      return;
    }

    final DirectoryBasedDartSdk dirBasedSdk = new DirectoryBasedDartSdk(new File(sdk.getHomePath()));

    final Set<Module> affectedModules = new HashSet<Module>();
    final SortedMap<String, Set<String>> packageNameToDirMap = new TreeMap<String, Set<String>>();

    final Runnable runnable = new Runnable() {
      public void run() {
        final Module[] modules = ModuleManager.getInstance(project).getModules();
        for (int i = 0; i < modules.length; i++) {
          final Module module = modules[i];

          final ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
          if (indicator != null) {
            indicator.setText("pub list-package-dirs");
            indicator.setText2("Module: " + module.getName());
            indicator.setIndeterminate(false);
            indicator.setFraction((i + 1.) / modules.length);
            indicator.checkCanceled();
          }

					/*if(DartSdkGlobalLibUtil.isDartSdkGlobalLibAttached(module, sdk.getGlobalLibName()))
          {
						for(VirtualFile contentRoot : ModuleRootManager.getInstance(module).getContentRoots())
						{
							if(contentRoot.findChild(PubspecYamlUtil.PUBSPEC_YAML) != null)
							{
								continue;
							}

							final File rootDir = new File(contentRoot.getPath());
							final Map<String, List<File>> map = new MyExplicitPackageUriResolver(dirBasedSdk, rootDir).calculatePackageMap();

							if(!map.isEmpty())
							{
								affectedModules.add(module);
								addResults(packageNameToDirMap, map);
							}
						}
					}   */
        }
      }
    };

    if (ProgressManager.getInstance().runProcessWithProgressSynchronously(runnable, "pub list-package-dirs", true, project)) {
      final DartListPackageDirsDialog dialog = new DartListPackageDirsDialog(project, packageNameToDirMap);
      dialog.show();

      if (dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
        configurePubListPackageDirsLibrary(project, affectedModules, packageNameToDirMap);
      }

      if (dialog.getExitCode() == DartListPackageDirsDialog.CONFIGURE_NONE_EXIT_CODE) {
        removePubListPackageDirsLibrary(project);
      }
    }
  }

  private static void addResults(final @Nonnull Map<String, Set<String>> packageNameToDirMap, final @Nonnull Map<String, List<File>> map) {
    for (Map.Entry<String, List<File>> entry : map.entrySet()) {
      final String packageName = entry.getKey();
      Set<String> packageRoots = packageNameToDirMap.get(packageName);

      if (packageRoots == null) {
        packageRoots = new HashSet<String>();
        packageNameToDirMap.put(packageName, packageRoots);
      }

      for (File file : entry.getValue()) {
        packageRoots.add(FileUtil.toSystemIndependentName(file.getPath()));
      }
    }
  }

  static void configurePubListPackageDirsLibrary(final @Nonnull Project project,
                                                 final @Nonnull Set<Module> modules,
                                                 final @Nonnull Map<String,
                                                   Set<String>> packageMap) {
    if (modules.isEmpty() || packageMap.isEmpty()) {
      removePubListPackageDirsLibrary(project);
      return;
    }

    ApplicationManager.getApplication().runWriteAction(new Runnable() {
      public void run() {
        doConfigurePubListPackageDirsLibrary(project, modules, packageMap);
      }
    });
  }

  private static void doConfigurePubListPackageDirsLibrary(final Project project, final Set<Module> modules, final Map<String,
    Set<String>> packageMap) {
    final Library library = createPubListPackageDirsLibrary(project, packageMap);

    for (final Module module : ModuleManager.getInstance(project).getModules()) {
      final ModifiableRootModel modifiableModel = ModuleRootManager.getInstance(module).getModifiableModel();
      try {
        OrderEntry existingEntry = null;
        for (final OrderEntry entry : modifiableModel.getOrderEntries()) {
          if (entry instanceof LibraryOrderEntry &&
            LibraryTablesRegistrar.PROJECT_LEVEL.equals(((LibraryOrderEntry)entry).getLibraryLevel()) &&
            PUB_LIST_PACKAGE_DIRS_LIB_NAME.equals(((LibraryOrderEntry)entry).getLibraryName())) {
            existingEntry = entry;
            break;
          }
        }


        final boolean contains = existingEntry != null;
        final boolean mustContain = modules.contains(module);

        if (contains != mustContain) {
          if (mustContain) {
            modifiableModel.addLibraryEntry(library);
          }
          else {
            modifiableModel.removeOrderEntry(existingEntry);
          }
        }

        if (modifiableModel.isChanged()) {
          modifiableModel.commit();
        }
      }
      finally {
        if (!modifiableModel.isDisposed()) {
          modifiableModel.dispose();
        }
      }
    }
  }

  private static Library createPubListPackageDirsLibrary(final Project project, final Map<String, Set<String>> packageMap) {
    LibraryTable projectLibraryTable = LibraryTablesRegistrar.getInstance().getLibraryTable(project);
    Library library = projectLibraryTable.getLibraryByName(PUB_LIST_PACKAGE_DIRS_LIB_NAME);
    if (library == null) {
      final LibraryTable.ModifiableModel libTableModel = projectLibraryTable.getModifiableModel();
      library = libTableModel.createLibrary(PUB_LIST_PACKAGE_DIRS_LIB_NAME, DartListPackageDirsLibraryType.LIBRARY_KIND);
      libTableModel.commit();
    }

    final Library.ModifiableModel libModel = library.getModifiableModel();
    try {
      for (String url : libModel.getUrls(BinariesOrderRootType.getInstance())) {
        libModel.removeRoot(url, BinariesOrderRootType.getInstance());
      }

      for (Set<String> packageDirs : packageMap.values()) {
        for (String packageDir : packageDirs) {
          libModel.addRoot(VirtualFileUtil.pathToUrl(packageDir), BinariesOrderRootType.getInstance());
        }
      }

      final DartListPackageDirsLibraryProperties libraryProperties = new DartListPackageDirsLibraryProperties();
      libraryProperties.setPackageNameToDirsMap(packageMap);
      libModel.setProperties(libraryProperties);

      libModel.commit();
    }
    finally {
      if (!Disposer.isDisposed(libModel)) {
        Disposer.dispose(libModel);
      }
    }
    return library;
  }

  static void removePubListPackageDirsLibrary(final @Nonnull Project project) {
    ApplicationManager.getApplication().runWriteAction(new Runnable() {
      public void run() {
        doRemovePubListPackageDirsLibrary(project);
      }
    });
  }

  private static void doRemovePubListPackageDirsLibrary(final Project project) {
    for (final Module module : ModuleManager.getInstance(project).getModules()) {
      final ModifiableRootModel modifiableModel = ModuleRootManager.getInstance(module).getModifiableModel();
      try {
        for (final OrderEntry entry : modifiableModel.getOrderEntries()) {
          if (entry instanceof LibraryOrderEntry &&
            LibraryTablesRegistrar.PROJECT_LEVEL.equals(((LibraryOrderEntry)entry).getLibraryLevel()) &&
            PUB_LIST_PACKAGE_DIRS_LIB_NAME.equals(((LibraryOrderEntry)entry).getLibraryName())) {
            modifiableModel.removeOrderEntry(entry);
          }
        }

        if (modifiableModel.isChanged()) {
          modifiableModel.commit();
        }
      }
      finally {
        if (!modifiableModel.isDisposed()) {
          modifiableModel.dispose();
        }
      }
    }

    LibraryTable projectLibraryTable = LibraryTablesRegistrar.getInstance().getLibraryTable(project);
    final Library library = projectLibraryTable.getLibraryByName(PUB_LIST_PACKAGE_DIRS_LIB_NAME);
    if (library != null) {
      projectLibraryTable.removeLibrary(library);
    }
  }
}

class MyExplicitPackageUriResolver extends ExplicitPackageUriResolver {
  public MyExplicitPackageUriResolver(final DirectoryBasedDartSdk sdk, final File rootDir) {
    super(sdk, rootDir);
  }

  // need public access to this method
  @Override
  public Map<String, List<File>> calculatePackageMap() {
    return super.calculatePackageMap();
  }
}