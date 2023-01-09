package com.jetbrains.lang.dart.ide.index;

import consulo.annotation.component.ExtensionImpl;
import consulo.index.io.DataIndexer;
import consulo.index.io.EnumeratorStringDescriptor;
import consulo.index.io.ID;
import consulo.index.io.KeyDescriptor;
import consulo.index.io.data.DataExternalizer;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.stub.FileBasedIndexExtension;
import consulo.language.psi.stub.FileContent;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;

import javax.annotation.Nonnull;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class DartPathIndex extends FileBasedIndexExtension<String, List<String>> {
  public static final ID<String, List<String>> DART_PATH_INDEX = ID.create("DartPathIndex");
  private static final int INDEX_VERSION = 2;
  private DataIndexer<String, List<String>, FileContent> myDataIndexer = new MyDataIndexer();

  @Nonnull
  @Override
  public ID<String, List<String>> getName() {
    return DART_PATH_INDEX;
  }

  @Override
  public int getVersion() {
    return DartIndexUtil.BASE_VERSION + INDEX_VERSION;
  }

  @Nonnull
  @Override
  public DataIndexer<String, List<String>, FileContent> getIndexer() {
    return myDataIndexer;
  }

  @Override
  public KeyDescriptor<String> getKeyDescriptor() {
    return new EnumeratorStringDescriptor();
  }

  @Override
  public DataExternalizer<List<String>> getValueExternalizer() {
    return new DataExternalizer<List<String>>() {
      @Override
      public void save(DataOutput out, List<String> value) throws IOException {
        out.writeInt(value.size());
        for (String path : value) {
          out.writeUTF(path);
        }
      }

      @Override
      public List<String> read(DataInput in) throws IOException {
        final int size = in.readInt();
        final List<String> result = new ArrayList<String>(size);
        for (int i = 0; i < size; ++i) {
          result.add(in.readUTF());
        }
        return result;
      }
    };
  }

  @Override
  public FileBasedIndex.InputFilter getInputFilter() {
    return DartInputFilter.INSTANCE;
  }

  @Override
  public boolean dependsOnFileContent() {
    return true;
  }

  public static List<String> getPaths(Project project, VirtualFile virtualFile) {
    final List<String> result = new ArrayList<String>();
    for (List<String> list : FileBasedIndex.getInstance()
                                           .getValues(DART_PATH_INDEX,
                                                      virtualFile.getName(),
                                                      GlobalSearchScope.fileScope(project, virtualFile))) {
      result.addAll(list);
    }
    return result;
  }

  private static class MyDataIndexer implements DataIndexer<String, List<String>, FileContent> {
    @Override
    @Nonnull
    public Map<String, List<String>> map(final FileContent inputData) {
      return Collections.singletonMap(inputData.getFileName(), DartIndexUtil.indexFile(inputData).getPaths());
    }
  }
}
