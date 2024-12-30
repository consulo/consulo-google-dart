package com.jetbrains.lang.dart.ide.index;

import com.jetbrains.lang.dart.psi.DartComponent;
import com.jetbrains.lang.dart.psi.DartComponentName;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.index.io.DataIndexer;
import consulo.index.io.EnumeratorStringDescriptor;
import consulo.index.io.ID;
import consulo.index.io.KeyDescriptor;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.stub.FileContent;
import consulo.language.psi.stub.ScalarIndexExtension;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;

import jakarta.annotation.Nonnull;
import java.util.*;

@ExtensionImpl
public class DartClassIndex extends ScalarIndexExtension<String> {
  public static final ID<String, Void> DART_CLASS_INDEX = ID.create("DartClassIndex");
  private static final int INDEX_VERSION = 2;
  private DataIndexer<String, Void, FileContent> myDataIndexer = new MyDataIndexer();

  @Nonnull
  @Override
  public ID<String, Void> getName() {
    return DART_CLASS_INDEX;
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

  @Override
  public KeyDescriptor<String> getKeyDescriptor() {
    return new EnumeratorStringDescriptor();
  }

  @Override
  public FileBasedIndex.InputFilter getInputFilter() {
    return DartInputFilter.INSTANCE;
  }

  @Override
  public boolean dependsOnFileContent() {
    return true;
  }

  public static Collection<String> getNames(Project project) {
    return FileBasedIndex.getInstance().getAllKeys(DART_CLASS_INDEX, project);
  }

  public static List<DartComponentName> getItemsByName(String name, Project project, GlobalSearchScope searchScope) {
    final Collection<VirtualFile> files = FileBasedIndex.getInstance().getContainingFiles(DART_CLASS_INDEX, name, searchScope);
    final Set<DartComponentName> result = new HashSet<DartComponentName>();
    for (VirtualFile vFile : files) {
      final PsiFile psiFile = PsiManager.getInstance(project).findFile(vFile);
      for (PsiElement root : DartResolveUtil.findDartRoots(psiFile)) {
        for (DartComponent component : DartResolveUtil.getClassDeclarations(root)) {
          if (name.equals(component.getName())) {
            result.add(component.getComponentName());
          }
        }
      }
    }
    return new ArrayList<DartComponentName>(result);
  }

  private static class MyDataIndexer implements DataIndexer<String, Void, FileContent> {
    @Override
    @Nonnull
    public Map<String, Void> map(final FileContent inputData) {
      DartFileIndexData indexData = DartIndexUtil.indexFile(inputData);
      final Map<String, Void> result = new HashMap<String, Void>();
      for (String componentName : indexData.getClassNames()) {
        result.put(componentName, null);
      }
      return result;
    }
  }
}
