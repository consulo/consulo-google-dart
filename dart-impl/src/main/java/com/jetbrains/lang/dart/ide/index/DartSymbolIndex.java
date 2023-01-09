package com.jetbrains.lang.dart.ide.index;

import com.jetbrains.lang.dart.psi.DartClass;
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
import consulo.language.psi.resolve.PsiElementProcessor;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.stub.FileContent;
import consulo.language.psi.stub.ScalarIndexExtension;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class DartSymbolIndex extends ScalarIndexExtension<String> {
  public static final ID<String, Void> DART_SYMBOL_INDEX = ID.create("DartSymbolIndex");
  private static final int INDEX_VERSION = 1;
  private DataIndexer<String, Void, FileContent> myDataIndexer = new MyDataIndexer();

  @Nonnull
  @Override
  public ID<String, Void> getName() {
    return DART_SYMBOL_INDEX;
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
    return FileBasedIndex.getInstance().getAllKeys(DART_SYMBOL_INDEX, project);
  }

  public static List<DartComponentName> getItemsByName(final String name, Project project, GlobalSearchScope searchScope) {
    final Collection<VirtualFile> files = FileBasedIndex.getInstance().getContainingFiles(DART_SYMBOL_INDEX, name, searchScope);
    final Set<DartComponentName> result = new HashSet<DartComponentName>();
    for (VirtualFile vFile : files) {
      final PsiFile psiFile = PsiManager.getInstance(project).findFile(vFile);
      for (PsiElement root : DartResolveUtil.findDartRoots(psiFile)) {
        processComponents(root, new PsiElementProcessor<DartComponent>() {
          @Override
          public boolean execute(@Nonnull DartComponent component) {
            if (name.equals(component.getName())) {
              result.add(component.getComponentName());
            }
            return true;
          }
        });
      }
    }
    return new ArrayList<DartComponentName>(result);
  }

  private static class MyDataIndexer implements DataIndexer<String, Void, FileContent> {
    @Override
    @Nonnull
    public Map<String, Void> map(final FileContent inputData) {
      List<String> symbols = DartIndexUtil.indexFile(inputData).getSymbols();
      final Map<String, Void> result = new HashMap<String, Void>();
      for (String symbol : symbols) {
        result.put(symbol, null);
      }
      return result;
    }
  }

  private static void processComponents(PsiElement context, PsiElementProcessor<DartComponent> processor) {
    final DartComponent[] components = PsiTreeUtil.getChildrenOfType(context, DartComponent.class);
    if (components == null) {
      return;
    }
    for (DartComponent component : components) {
      final String componentName = component.getName();
      if (componentName == null) {
        continue;
      }
      if (component instanceof DartClass) {
        for (DartComponent subComponent : DartResolveUtil.getNamedSubComponents((DartClass)component)) {
          if (!processor.execute(subComponent)) {
            return;
          }
        }
      }
      else {
        if (!processor.execute(component)) {
          return;
        }
      }
    }
  }
}
