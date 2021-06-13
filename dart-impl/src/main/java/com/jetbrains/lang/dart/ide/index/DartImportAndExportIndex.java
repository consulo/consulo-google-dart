package com.jetbrains.lang.dart.ide.index;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.*;
import com.intellij.util.io.DataExternalizer;
import com.intellij.util.io.EnumeratorStringDescriptor;
import com.intellij.util.io.KeyDescriptor;

import javax.annotation.Nonnull;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;

public class DartImportAndExportIndex extends FileBasedIndexExtension<String, List<DartImportOrExportInfo>>
{
	public static final ID<String, List<DartImportOrExportInfo>> DART_IMPORT_EXPORT_INDEX = ID.create("DartImportIndex");
	private static final int INDEX_VERSION = 2;
	private DataIndexer<String, List<DartImportOrExportInfo>, FileContent> myDataIndexer = new MyDataIndexer();

	@Nonnull
	@Override
	public ID<String, List<DartImportOrExportInfo>> getName()
	{
		return DART_IMPORT_EXPORT_INDEX;
	}

	@Override
	public int getVersion()
	{
		return DartIndexUtil.BASE_VERSION + INDEX_VERSION;
	}

	@Nonnull
	@Override
	public DataIndexer<String, List<DartImportOrExportInfo>, FileContent> getIndexer()
	{
		return myDataIndexer;
	}

	@Nonnull
	@Override
	public KeyDescriptor<String> getKeyDescriptor()
	{
		return new EnumeratorStringDescriptor();
	}

	@Nonnull
	@Override
	public DataExternalizer<List<DartImportOrExportInfo>> getValueExternalizer()
	{
		return new DataExternalizer<List<DartImportOrExportInfo>>()
		{
			@Override
			public void save(final @Nonnull DataOutput out, final @Nonnull List<DartImportOrExportInfo> value) throws IOException
			{
				out.writeInt(value.size());
				for(DartImportOrExportInfo importOrExportInfo : value)
				{
					out.writeUTF(importOrExportInfo.getKind().name());
					out.writeUTF(importOrExportInfo.getUri());
					out.writeUTF(StringUtil.notNullize(importOrExportInfo.getImportPrefix()));
					out.writeInt(importOrExportInfo.getShowComponents().size());
					for(String showComponentName : importOrExportInfo.getShowComponents())
					{
						out.writeUTF(showComponentName);
					}
					out.writeInt(importOrExportInfo.getHideComponents().size());
					for(String hideComponentName : importOrExportInfo.getHideComponents())
					{
						out.writeUTF(hideComponentName);
					}
				}
			}

			@Override
			@Nonnull
			public List<DartImportOrExportInfo> read(final @Nonnull DataInput in) throws IOException
			{
				final int size = in.readInt();
				final List<DartImportOrExportInfo> result = new ArrayList<DartImportOrExportInfo>(size);
				for(int i = 0; i < size; ++i)
				{
					final DartImportOrExportInfo.Kind kind = DartImportOrExportInfo.Kind.valueOf(in.readUTF());
					final String uri = in.readUTF();
					final String prefix = in.readUTF();
					final int showSize = in.readInt();
					final Set<String> showComponentNames = showSize == 0 ? Collections.<String>emptySet() : new HashSet<String>(showSize);
					for(int j = 0; j < showSize; j++)
					{
						showComponentNames.add(in.readUTF());
					}
					final int hideSize = in.readInt();
					final Set<String> hideComponentNames = hideSize == 0 ? Collections.<String>emptySet() : new HashSet<String>(hideSize);
					for(int j = 0; j < hideSize; j++)
					{
						hideComponentNames.add(in.readUTF());
					}
					result.add(new DartImportOrExportInfo(kind, uri, StringUtil.nullize(prefix), showComponentNames, hideComponentNames));
				}
				return result;
			}
		};
	}

	@Nonnull
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

	@Nonnull
	public static List<DartImportOrExportInfo> getImportAndExportInfos(final @Nonnull Project project, final @Nonnull VirtualFile virtualFile)
	{
		final List<DartImportOrExportInfo> result = new ArrayList<DartImportOrExportInfo>();
		for(List<DartImportOrExportInfo> list : FileBasedIndex.getInstance().getValues(DART_IMPORT_EXPORT_INDEX, virtualFile.getName(),
				GlobalSearchScope.fileScope(project, virtualFile)))
		{
			result.addAll(list);
		}
		return result;
	}

	private static class MyDataIndexer implements DataIndexer<String, List<DartImportOrExportInfo>, FileContent>
	{
		@Override
		@Nonnull
		public Map<String, List<DartImportOrExportInfo>> map(@Nonnull final FileContent inputData)
		{
			return Collections.singletonMap(inputData.getFileName(), DartIndexUtil.indexFile(inputData).getImportAndExportInfos());
		}
	}
}
