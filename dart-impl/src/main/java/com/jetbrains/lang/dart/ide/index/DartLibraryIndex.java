package com.jetbrains.lang.dart.ide.index;

import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.psi.*;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.ApplicationManager;
import consulo.application.util.function.Computable;
import consulo.content.bundle.Sdk;
import consulo.dart.module.extension.DartModuleExtension;
import consulo.index.io.DataIndexer;
import consulo.index.io.EnumeratorStringDescriptor;
import consulo.index.io.ID;
import consulo.index.io.KeyDescriptor;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiFileFactory;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.stub.FileContent;
import consulo.language.psi.stub.ScalarIndexExtension;
import consulo.language.util.ModuleUtilCore;
import consulo.project.Project;
import consulo.util.collection.BidirectionalMap;
import consulo.util.dataholder.Key;
import consulo.util.lang.Pair;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

@ExtensionImpl
public class DartLibraryIndex extends ScalarIndexExtension<String> {
  public static final ID<String, Void> DART_LIBRARY_INDEX = ID.create("DartLibraryIndex");
  private static final int INDEX_VERSION = 2;

  private static final Key<Pair<Long, consulo.util.collection.BidirectionalMap<String, String>>> LIBRARIES_TIME_AND_MAP_KEY =
    Key.create("dart.internal.libraries");

  private DataIndexer<String, Void, FileContent> myDataIndexer = new MyDataIndexer();

  @Nonnull
  @Override
  public ID<String, Void> getName() {
    return DART_LIBRARY_INDEX;
  }

  @Override
  public int getVersion() {
    return DartIndexUtil.BASE_VERSION + INDEX_VERSION;
  }

  @Nonnull
  @Override
  public DataIndexer<String, Void, FileContent> getIndexer() {
    return myDataIndexer;
  }

  @Nonnull
  @Override
  public KeyDescriptor<String> getKeyDescriptor() {
    return new EnumeratorStringDescriptor();
  }

  @Nonnull
  @Override
  public FileBasedIndex.InputFilter getInputFilter() {
    return DartInputFilter.INSTANCE;
  }

  @Override
  public boolean dependsOnFileContent() {
    return true;
  }

  public static List<VirtualFile> findLibraryClass(@Nonnull PsiElement context, String libraryName) {
    if (libraryName.startsWith("dart:")) {
      String stdLibName = libraryName.substring("dart:".length());
      VirtualFile stdLibFile = getStandardLibraryFromSdk(context, stdLibName);
      if (stdLibFile != null) {
        return Arrays.asList(stdLibFile);
      }
    }
    return new ArrayList<VirtualFile>(findSingleLibraryClass(context.getProject(), libraryName));
  }

  private static Collection<VirtualFile> findSingleLibraryClass(Project project, String libraryName) {
    return FileBasedIndex.getInstance().getContainingFiles(DART_LIBRARY_INDEX, libraryName, GlobalSearchScope.allScope(project));
  }

  public static Collection<VirtualFile> findSingleLibraryClass(String libraryName, GlobalSearchScope scope) {
    return FileBasedIndex.getInstance().getContainingFiles(DART_LIBRARY_INDEX, libraryName, scope);
  }

  @Nonnull
  public static Set<String> getAllLibraryNames(Project project) {
    final Collection<String> allKeys = FileBasedIndex.getInstance().getAllKeys(DART_LIBRARY_INDEX, project);
    return new HashSet<String>(allKeys);
  }

  private static class MyDataIndexer implements DataIndexer<String, Void, FileContent> {
    @Override
    @Nonnull
    public Map<String, Void> map(@Nonnull final FileContent inputData) {
      final String libraryName = DartIndexUtil.indexFile(inputData).getLibraryName();
      return libraryName == null ? Collections.<String, Void>emptyMap() : Collections.<String, Void>singletonMap(libraryName, null);
    }
  }

  @Nullable
  public static String getStandardLibraryNameByRelativePath(final @Nonnull Project project, Sdk sdk, final @Nonnull String relativePath) {
    final List<String> libNames = sdk == null ? null : getLibraryNameToRelativePathMap(project, sdk).getKeysByValue(relativePath);
    return libNames == null || libNames.isEmpty() ? null : libNames.get(0);
  }

  @Nullable
  public static VirtualFile getStandardLibraryFromSdk(final @Nonnull PsiElement element, final @Nonnull String libraryName) {
    Project project = element.getProject();
    Sdk sdk = ModuleUtilCore.getSdk(element, DartModuleExtension.class);
    final String relativeLibPath = sdk == null ? null : getLibraryNameToRelativePathMap(project, sdk).get(libraryName);
    return relativeLibPath == null ? null : LocalFileSystem.getInstance().findFileByPath(sdk.getHomePath() + "/lib/" + relativeLibPath);
  }

  @Nullable
  public static VirtualFile getStandardLibraryFromSdk(final @Nonnull Project project, Sdk sdk, final @Nonnull String libraryName) {
    final String relativeLibPath = sdk == null ? null : getLibraryNameToRelativePathMap(project, sdk).get(libraryName);
    return relativeLibPath == null ? null : LocalFileSystem.getInstance().findFileByPath(sdk.getHomePath() + "/lib/" + relativeLibPath);
  }

  public static Collection<String> getAllStandardLibrariesFromSdk(final @Nonnull PsiElement e) {
    Sdk sdk = ModuleUtilCore.getSdk(e, DartModuleExtension.class);
    return sdk == null ? Collections.<String>emptyList() : getLibraryNameToRelativePathMap(e.getProject(), sdk).keySet();
  }

  @Nonnull
  private static consulo.util.collection.BidirectionalMap<String, String> getLibraryNameToRelativePathMap(final @Nonnull Project project,
                                                                                                          final @Nonnull Sdk sdk) {
    final VirtualFile librariesDartFile = LocalFileSystem.getInstance().findFileByPath(sdk.getHomePath() + "/lib/_internal/libraries.dart");
    if (librariesDartFile == null) {
      return new consulo.util.collection.BidirectionalMap<String, String>();
    }

    final Pair<Long, consulo.util.collection.BidirectionalMap<String, String>> data =
      librariesDartFile.getUserData(LIBRARIES_TIME_AND_MAP_KEY);
    final Long cachedTimestamp = data == null ? null : data.first;
    final long modificationCount = librariesDartFile.getModificationCount();

    if (cachedTimestamp != null && cachedTimestamp.equals(modificationCount)) {
      return data.second;
    }

    return ApplicationManager.getApplication().runReadAction(new Computable<consulo.util.collection.BidirectionalMap<String, String>>() {
      public BidirectionalMap<String, String> compute() {
        final String contents = StringUtil.convertLineSeparators(librariesDartFile.loadText().toString());
        final PsiFile psiFile = PsiFileFactory.getInstance(project).createFileFromText("libraries.dart", DartLanguage.INSTANCE,
                                                                                       contents);
        if (!(psiFile instanceof DartFile)) {
          return new BidirectionalMap<String, String>();
        }

        final Pair<Long, consulo.util.collection.BidirectionalMap<String, String>> data = Pair.create(modificationCount,
                                                                                                      computeLibraryNameToRelativePathMap(
                                                                                                        (DartFile)psiFile));
        librariesDartFile.putUserData(LIBRARIES_TIME_AND_MAP_KEY, data);
        return data.second;
      }
    });
  }

  private static BidirectionalMap<String, String> computeLibraryNameToRelativePathMap(final @Nonnull DartFile librariesDartFile) {
/*
const Map<String, LibraryInfo> LIBRARIES = const {

  "async": const LibraryInfo(
      "async/async.dart",
      maturity: Maturity.STABLE,
      dart2jsPatchPath: "_internal/lib/async_patch.dart"),

  "_chrome": const LibraryInfo(
      "_chrome/dart2js/chrome_dart2js.dart",
      documented: false,
      category: "Client"),

*/
    final consulo.util.collection.BidirectionalMap<String, String> result = new BidirectionalMap<String, String>();

    librariesDartFile.acceptChildren(new DartRecursiveVisitor() {
      public void visitMapLiteralEntry(final @Nonnull DartMapLiteralEntry mapLiteralEntry) {
        final List<DartExpression> expressions = mapLiteralEntry.getExpressionList();
        if (expressions.size() != 2 ||
          !(expressions.get(0) instanceof DartStringLiteralExpression) ||
          !(expressions.get(1) instanceof DartConstConstructorExpression)) {
          return;
        }

        final DartStringLiteralExpression keyExpression = (DartStringLiteralExpression)expressions.get(0);
        final DartConstConstructorExpression constructorExpression = (DartConstConstructorExpression)expressions.get(1);

        final String libraryName = StringUtil.unquoteString(keyExpression.getText());
        if (libraryName.startsWith("_")) {
          return;
        }

        final DartType dartType = constructorExpression.getType();
        if (dartType == null || !"LibraryInfo".equals(dartType.getText())) {
          return;
        }

        final DartArguments arguments = constructorExpression.getArguments();
        final DartArgumentList argumentList = arguments != null ? arguments.getArgumentList() : null;
        final List<DartExpression> expressionList = argumentList != null ? argumentList.getExpressionList() : null;
        final DartExpression firstExpression = expressionList == null || expressionList.isEmpty() ? null : expressionList.get(0);
        final String libraryRelativePath = firstExpression instanceof DartStringLiteralExpression ? StringUtil.unquoteString(firstExpression
                                                                                                                               .getText()) : null;

        if (libraryRelativePath != null) {
          result.put(libraryName, libraryRelativePath);
        }
      }
    });

    return result;
  }
}
