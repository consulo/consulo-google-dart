package com.jetbrains.lang.dart.ide.index;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.util.function.Processor;
import consulo.index.io.DataIndexer;
import consulo.index.io.EnumeratorStringDescriptor;
import consulo.index.io.ID;
import consulo.index.io.KeyDescriptor;
import consulo.index.io.data.DataExternalizer;
import consulo.language.psi.PsiElement;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.stub.FileBasedIndexExtension;
import consulo.language.psi.stub.FileContent;
import consulo.project.Project;
import consulo.util.lang.function.Condition;
import consulo.util.lang.function.PairProcessor;
import consulo.virtualFileSystem.VirtualFile;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.*;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class DartComponentIndex extends FileBasedIndexExtension<String, DartComponentInfo> {
  public static final ID<String, DartComponentInfo> DART_COMPONENT_INDEX = ID.create("DartComponentIndex");
  private static final int INDEX_VERSION = 3;
  private final DataIndexer<String, DartComponentInfo, FileContent> myIndexer = new MyDataIndexer();
  private final DataExternalizer<DartComponentInfo> myExternalizer = new DartComponentInfoExternalizer();

  @Nonnull
  @Override
  public ID<String, DartComponentInfo> getName() {
    return DART_COMPONENT_INDEX;
  }

  @Nonnull
  @Override
  public DataIndexer<String, DartComponentInfo, FileContent> getIndexer() {
    return myIndexer;
  }

  @Override
  public KeyDescriptor<String> getKeyDescriptor() {
    return new EnumeratorStringDescriptor();
  }

  @Override
  public DataExternalizer<DartComponentInfo> getValueExternalizer() {
    return myExternalizer;
  }

  @Override
  public FileBasedIndex.InputFilter getInputFilter() {
    return DartInputFilter.INSTANCE;
  }

  @Override
  public boolean dependsOnFileContent() {
    return true;
  }

  @Override
  public int getVersion() {
    return DartIndexUtil.BASE_VERSION + INDEX_VERSION;
  }

  public static List<VirtualFile> getAllFiles(@Nonnull Project project, @Nullable String componentName) {
    if (componentName == null) {
      return Collections.emptyList();
    }
    return new ArrayList<VirtualFile>(FileBasedIndex.getInstance()
                                                    .getContainingFiles(DART_COMPONENT_INDEX,
                                                                        componentName,
                                                                        GlobalSearchScope.allScope(project)));
  }

  public static void processAllComponents(@Nonnull PsiElement contex,
                                          final PairProcessor<String, DartComponentInfo> processor,
                                          Condition<String> nameFilter) {
    final Collection<String> allKeys = FileBasedIndex.getInstance().getAllKeys(DART_COMPONENT_INDEX, contex.getProject());
    for (final String componentName : allKeys) {
      if (nameFilter.value(componentName)) {
        continue;
      }
      if (processComponentsByName(contex, new Processor<DartComponentInfo>() {
        @Override
        public boolean process(DartComponentInfo info) {
          return processor.process(componentName, info);
        }
      }, componentName)) {
        return;
      }
    }
  }

  public static boolean processComponentsByName(PsiElement contex, Processor<DartComponentInfo> processor, String componentName) {
    final List<DartComponentInfo> allComponents =
      FileBasedIndex.getInstance().getValues(DART_COMPONENT_INDEX, componentName, contex.getResolveScope());
    for (DartComponentInfo componentInfo : allComponents) {
      if (!processor.process(componentInfo)) {
        return true;
      }
    }
    return false;
  }

  private static class MyDataIndexer implements DataIndexer<String, DartComponentInfo, FileContent> {
    @Nonnull
    @Override
    public Map<String, DartComponentInfo> map(FileContent inputData) {
      return DartIndexUtil.indexFile(inputData).getComponentInfoMap();
    }
  }
}
