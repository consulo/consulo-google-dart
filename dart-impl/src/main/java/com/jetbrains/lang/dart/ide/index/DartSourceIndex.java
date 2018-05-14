package com.jetbrains.lang.dart.ide.index;

import gnu.trove.THashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

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

/**
 * @author: Fedor.Korotkov
 */
public class DartSourceIndex extends ScalarIndexExtension<String>
{
	public static final ID<String, Void> DART_SOURCE_INDEX = ID.create("DartSourceIndex");
	private static final int INDEX_VERSION = 2;
	private DataIndexer<String, Void, FileContent> myDataIndexer = new MyDataIndexer();

	@Nonnull
	@Override
	public ID<String, Void> getName()
	{
		return DART_SOURCE_INDEX;
	}

	@Override
	public int getVersion()
	{
		return DartIndexUtil.BASE_VERSION + INDEX_VERSION;
	}

	@Nonnull
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

	public static List<VirtualFile> findLibraries(final PsiElement context, @Nonnull String fileName, @Nonnull final GlobalSearchScope scope)
	{
		return new ArrayList<VirtualFile>(FileBasedIndex.getInstance().getContainingFiles(DART_SOURCE_INDEX, fileName, scope));
	}

	private static class MyDataIndexer implements DataIndexer<String, Void, FileContent>
	{
		@Override
		@Nonnull
		public Map<String, Void> map(final FileContent inputData)
		{
			final Map<String, Void> result = new THashMap<String, Void>();
			for(String pathValue : DartIndexUtil.indexFile(inputData).getPaths())
			{
				result.put(pathValue.substring(pathValue.lastIndexOf('/') + 1), null);
			}
			return result;
		}
	}
}
