package com.jetbrains.lang.dart.ide.index;

import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponent;
import com.jetbrains.lang.dart.psi.DartComponentName;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.ApplicationManager;
import consulo.application.util.function.Computable;
import consulo.application.util.function.Processor;
import consulo.application.util.query.QueryExecutor;
import consulo.index.io.DataIndexer;
import consulo.index.io.EnumeratorStringDescriptor;
import consulo.index.io.ID;
import consulo.index.io.KeyDescriptor;
import consulo.index.io.data.DataExternalizer;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.search.DefinitionsScopedSearch;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.stub.FileBasedIndexExtension;
import consulo.language.psi.stub.FileContent;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.virtualFileSystem.VirtualFile;

import jakarta.annotation.Nonnull;
import java.util.*;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class DartInheritanceIndex extends FileBasedIndexExtension<String, List<DartComponentInfo>> {
  public static final ID<String, List<DartComponentInfo>> DART_INHERITANCE_INDEX = ID.create("DartInheritanceIndex");
  private static final int INDEX_VERSION = 2;
  private final DataIndexer<String, List<DartComponentInfo>, FileContent> myIndexer = new MyDataIndexer();
  private final DataExternalizer<List<DartComponentInfo>> myExternalizer = new DartComponentInfoListExternalizer();

  @Nonnull
  @Override
  public ID<String, List<DartComponentInfo>> getName() {
    return DART_INHERITANCE_INDEX;
  }

  @Override
  public int getVersion() {
    return DartIndexUtil.BASE_VERSION + INDEX_VERSION;
  }

  @Override
  public boolean dependsOnFileContent() {
    return true;
  }

  @Override
  public KeyDescriptor<String> getKeyDescriptor() {
    return new EnumeratorStringDescriptor();
  }

  @Override
  public DataExternalizer<List<DartComponentInfo>> getValueExternalizer() {
    return myExternalizer;
  }

  @Override
  public FileBasedIndex.InputFilter getInputFilter() {
    return DartInputFilter.INSTANCE;
  }

  @Nonnull
  @Override
  public DataIndexer<String, List<DartComponentInfo>, FileContent> getIndexer() {
    return myIndexer;
  }

  private static class MyDataIndexer implements DataIndexer<String, List<DartComponentInfo>, FileContent> {
    @Override
    @Nonnull
    public Map<String, List<DartComponentInfo>> map(final FileContent inputData) {
      return DartIndexUtil.indexFile(inputData).getInheritorsMap();
    }
  }

  public static List<DartClass> getItemsByName(final DartClass dartClass) {
    final List<DartClass> result = new ArrayList<DartClass>();
    DefinitionsScopedSearch.search(dartClass).forEach(new Processor<PsiElement>() {
      @Override
      public boolean process(PsiElement element) {
        if (element instanceof DartClass) {
          result.add((DartClass)element);
        }
        return true;
      }
    });
    return result;
  }

}
