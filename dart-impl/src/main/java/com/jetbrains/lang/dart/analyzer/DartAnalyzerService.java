package com.jetbrains.lang.dart.analyzer;

import com.google.dart.engine.AnalysisEngine;
import com.google.dart.engine.context.AnalysisContext;
import com.google.dart.engine.context.ChangeSet;
import com.google.dart.engine.sdk.DirectoryBasedDartSdk;
import com.google.dart.engine.source.DartUriResolver;
import com.google.dart.engine.source.ExplicitPackageUriResolver;
import com.google.dart.engine.source.Source;
import com.google.dart.engine.source.SourceFactory;
import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.sdk.DartConfigurable;
import com.jetbrains.lang.dart.util.DartUrlResolver;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.annotation.component.ServiceImpl;
import consulo.application.ApplicationManager;
import consulo.disposer.Disposer;
import consulo.document.Document;
import consulo.document.FileDocumentManager;
import consulo.ide.ServiceManager;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.module.content.ProjectRootManager;
import consulo.project.Project;
import consulo.util.io.FileUtil;
import consulo.util.lang.Comparing;
import consulo.util.lang.ref.SoftReference;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.event.*;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.Function;

@ServiceAPI(ComponentScope.PROJECT)
@ServiceImpl
@Singleton
public class DartAnalyzerService {

  private final Project myProject;

  private
  @Nullable
  String mySdkPath;
  private long myPubspecYamlTimestamp;
  private
  @Nonnull
  VirtualFile[] myDartPackageRoots;
  private
  @Nullable
  VirtualFile myContentRoot; // checked only in case of ExplicitPackageUriResolver

  private
  @Nullable
  WeakReference<AnalysisContext> myAnalysisContextRef;

  private final Collection<VirtualFile> myCreatedFiles = Collections.synchronizedSet(new HashSet<VirtualFile>());

  private final Map<VirtualFile, DartFileBasedSource> myFileToSourceMap = Collections.synchronizedMap(new HashMap<VirtualFile,
    DartFileBasedSource>());

  @Inject
  public DartAnalyzerService(final Project project) {
    myProject = project;

    final VirtualFileListener listener = new VirtualFileListener() {
      public void beforePropertyChange(@Nonnull final VirtualFilePropertyEvent event) {
        if (VirtualFile.PROP_NAME.equals(event.getPropertyName())) {
          fileDeleted(event);
        }
      }

      public void beforeFileMovement(@Nonnull final VirtualFileMoveEvent event) {
        fileDeleted(event);
      }

      public void fileDeleted(@Nonnull final VirtualFileEvent event) {
        if (FileUtil.extensionEquals(event.getFileName(), DartFileType.DEFAULT_EXTENSION)) {
          myFileToSourceMap.remove(event.getFile());
        }
      }

      public void propertyChanged(@Nonnull final VirtualFilePropertyEvent event) {
        if (VirtualFile.PROP_NAME.equals(event.getPropertyName())) {
          fileCreated(event);
        }
      }

      public void fileMoved(@Nonnull final VirtualFileMoveEvent event) {
        fileCreated(event);
      }

      public void fileCopied(@Nonnull final VirtualFileCopyEvent event) {
        fileCreated(event);
      }

      public void fileCreated(@Nonnull final VirtualFileEvent event) {
        if (FileUtil.extensionEquals(event.getFileName(), DartFileType.DEFAULT_EXTENSION)) {
          myCreatedFiles.add(event.getFile());
        }
      }
    };

    LocalFileSystem.getInstance().addVirtualFileListener(listener);

    Disposer.register(project, () -> LocalFileSystem.getInstance().removeVirtualFileListener(listener));
  }

  @Nonnull
  public static DartAnalyzerService getInstance(final @Nonnull Project project) {
    return ServiceManager.getService(project, DartAnalyzerService.class);
  }

  @Nonnull
  public AnalysisContext getAnalysisContext(final @Nonnull VirtualFile annotatedFile, final @Nonnull String sdkPath) {
    AnalysisContext analysisContext = SoftReference.dereference(myAnalysisContextRef);

    final DartUrlResolver dartUrlResolver = DartUrlResolver.getInstance(myProject, annotatedFile);
    final VirtualFile yamlFile = dartUrlResolver.getPubspecYamlFile();
    final Document cachedDocument = yamlFile == null ? null : FileDocumentManager.getInstance().getCachedDocument(yamlFile);
    final long pubspecYamlTimestamp = yamlFile == null ? -1 : cachedDocument == null ? yamlFile.getModificationCount() : cachedDocument
      .getModificationStamp();

    final VirtualFile[] packageRoots = dartUrlResolver.getPackageRoots();

    final VirtualFile contentRoot = ProjectRootManager.getInstance(myProject).getFileIndex().getContentRootForFile(annotatedFile);
    final Module module = ModuleUtilCore.findModuleForFile(annotatedFile, myProject);

    final boolean useExplicitPackageUriResolver = !ApplicationManager.getApplication().isUnitTestMode() &&
      contentRoot != null &&
      module != null &&
      !DartConfigurable.isCustomPackageRootSet(module) &&
      yamlFile == null;

    final boolean sameContext = analysisContext != null &&
      Comparing.equal(sdkPath, mySdkPath) &&
      pubspecYamlTimestamp == myPubspecYamlTimestamp &&
      Comparing.haveEqualElements(packageRoots, myDartPackageRoots) &&
      (!useExplicitPackageUriResolver || Comparing.equal(contentRoot, myContentRoot));

    if (sameContext) {
      applyChangeSet(analysisContext, annotatedFile);
      myCreatedFiles.clear();
    }
    else {
      final DirectoryBasedDartSdk dirBasedSdk = new DirectoryBasedDartSdk(new File(sdkPath));
      final DartUriResolver dartUriResolver = new DartUriResolver(dirBasedSdk);
      final DartFileAndPackageUriResolver fileAndPackageUriResolver = new DartFileAndPackageUriResolver(myProject, dartUrlResolver);

      final SourceFactory sourceFactory = useExplicitPackageUriResolver ? new SourceFactory(dartUriResolver, fileAndPackageUriResolver,
                                                                                            new ExplicitPackageUriResolver(dirBasedSdk,
                                                                                                                           new File(
                                                                                                                             contentRoot.getPath()))) : new SourceFactory(
        dartUriResolver,
        fileAndPackageUriResolver);

      analysisContext = AnalysisEngine.getInstance().createAnalysisContext();
      analysisContext.setSourceFactory(sourceFactory);

      mySdkPath = sdkPath;
      myPubspecYamlTimestamp = pubspecYamlTimestamp;
      myDartPackageRoots = packageRoots;
      myContentRoot = contentRoot;
      myAnalysisContextRef = new WeakReference<AnalysisContext>(analysisContext);
    }

    return analysisContext;
  }

  private void applyChangeSet(final AnalysisContext context, final VirtualFile annotatedFile) {
    final ChangeSet changeSet = new ChangeSet();

    final DartFileBasedSource source = myFileToSourceMap.get(annotatedFile);
    if (source != null) {
      handleDeletedAndOutOfDateSources(changeSet, source);
    }

    handleDeletedAndOutOfDateSources(changeSet, context.getLibrarySources());
    handleDeletedAndOutOfDateSources(changeSet, context.getHtmlSources());

    synchronized (myCreatedFiles) {
      for (VirtualFile file : myCreatedFiles) {
        changeSet.addedSource(DartFileBasedSource.getSource(myProject, file));
      }
    }

    context.applyChanges(changeSet);
  }

  private void handleDeletedAndOutOfDateSources(final ChangeSet changeSet, final Source... sources) {
    for (final Source source : sources) {
      if (source instanceof DartFileBasedSource) {
        if (!source.exists() || !myFileToSourceMap.containsKey(((DartFileBasedSource)source).getFile())) {
          changeSet.removedSource(source);
          continue;
        }

        if (((DartFileBasedSource)source).isOutOfDate()) {
          changeSet.changedSource(source);
        }
      }
    }
  }

  /**
   * Do not use this method directly, use {@link com.jetbrains.lang.dart.analyzer.DartFileBasedSource#getSource(Project, VirtualFile)}
   */
  @Nonnull
  DartFileBasedSource getOrCreateSource(final @Nonnull VirtualFile file,
                                        final @Nonnull Function<VirtualFile, DartFileBasedSource> creator) {
    DartFileBasedSource source = myFileToSourceMap.get(file);
    if (source == null) {
      source = creator.apply(file);
      myFileToSourceMap.put(file, source);
    }
    return source;
  }
}
