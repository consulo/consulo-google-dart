package com.jetbrains.lang.dart.ide.index;

import gnu.trove.THashSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.DataIndexer;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.util.indexing.FileContent;
import com.intellij.util.indexing.ID;
import com.intellij.util.indexing.ScalarIndexExtension;
import com.intellij.util.io.EnumeratorStringDescriptor;
import com.intellij.util.io.KeyDescriptor;
import com.jetbrains.lang.dart.ide.module.DartModuleExtension;
import com.jetbrains.lang.dart.ide.settings.DartSdkUtil;

/**
 * @author: Fedor.Korotkov
 */
public class DartLibraryIndex extends ScalarIndexExtension<String>
{
	public static final ID<String, Void> DART_LIBRARY_INDEX = ID.create("DartLibraryIndex");
	private static final int INDEX_VERSION = 2;
	private DataIndexer<String, Void, FileContent> myDataIndexer = new MyDataIndexer();

	@NotNull
	@Override
	public ID<String, Void> getName()
	{
		return DART_LIBRARY_INDEX;
	}

	@Override
	public int getVersion()
	{
		return DartIndexUtil.BASE_VERSION + INDEX_VERSION;
	}

	@NotNull
	@Override
	public DataIndexer<String, Void, FileContent> getIndexer()
	{
		return myDataIndexer;
	}

	@Override
	public KeyDescriptor<String> getKeyDescriptor()
	{
		return new EnumeratorStringDescriptor();
	}

	@Override
	public FileBasedIndex.InputFilter getInputFilter()
	{
		return DartInputFilter.INSTANCE;
	}

	@Override
	public boolean dependsOnFileContent()
	{
		return true;
	}

	public static List<VirtualFile> findLibraryClass(@NotNull PsiElement context, String libraryName)
	{
		if(libraryName.startsWith("dart:"))
		{
			Module moduleForPsiElement = ModuleUtilCore.findModuleForPsiElement(context);
			if(moduleForPsiElement != null)
			{
				Sdk sdk = ModuleUtilCore.getSdk(moduleForPsiElement, DartModuleExtension.class);
				if(sdk != null)
				{
					String stdLibName = libraryName.substring("dart:".length());
					VirtualFile stdLibFile = DartSdkUtil.findSdkLibrary(sdk, context, stdLibName);
					if(stdLibFile != null)
					{
						return Arrays.asList(stdLibFile);
					}
				}
			}
		}
		return new ArrayList<VirtualFile>(findSingleLibraryClass(context.getProject(), libraryName));
	}

	private static Collection<VirtualFile> findSingleLibraryClass(Project project, String libraryName)
	{
		return FileBasedIndex.getInstance().getContainingFiles(DART_LIBRARY_INDEX, libraryName, GlobalSearchScope.allScope(project));
	}

	public static Collection<VirtualFile> findSingleLibraryClass(String libraryName, GlobalSearchScope scope)
	{
		return FileBasedIndex.getInstance().getContainingFiles(DART_LIBRARY_INDEX, libraryName, scope);
	}

	@NotNull
	public static Set<String> getAllLibraryNames(Project project)
	{
		final Collection<String> allKeys = FileBasedIndex.getInstance().getAllKeys(DART_LIBRARY_INDEX, project);
		return new THashSet<String>(allKeys);
	}

	private static class MyDataIndexer implements DataIndexer<String, Void, FileContent>
	{
		@Override
		@NotNull
		public Map<String, Void> map(final FileContent inputData)
		{
			final String libraryName = DartIndexUtil.indexFile(inputData).getLibraryName();
			return libraryName == null ? Collections.<String, Void>emptyMap() : Collections.<String, Void>singletonMap(libraryName, null);
		}
	}
}
