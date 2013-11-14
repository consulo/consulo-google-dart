package com.jetbrains.lang.dart.analyzer;

import java.net.URI;

import com.google.dart.engine.source.ContentCache;
import com.google.dart.engine.source.Source;
import com.google.dart.engine.source.UriKind;
import com.google.dart.engine.source.UriResolver;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

public class DartFileResolver extends UriResolver
{

	private final Project myProject;

	public DartFileResolver(final Project project)
	{
		myProject = project;
	}

	@Override
	public Source fromEncoding(final ContentCache contentCache, final UriKind kind, final URI uri)
	{
		if(kind == UriKind.FILE_URI)
		{
			final VirtualFile file = LocalFileSystem.getInstance().findFileByPath(uri.getPath());
			if(file != null)
			{
				return DartFileBasedSource.getSource(myProject, file);
			}
			else
			{
				DartInProcessAnnotator.LOG.debug("DartFileResolver.fromEncoding: file not found: " + uri);
			}
		}
		return null;
	}

	@Override
	public Source resolveAbsolute(final ContentCache contentCache, final URI uri)
	{
		if("file".equals(uri.getScheme()))
		{
			final VirtualFile file = LocalFileSystem.getInstance().findFileByPath(uri.getPath());
			if(file != null)
			{
				return DartFileBasedSource.getSource(myProject, file);
			}
			else
			{
				DartInProcessAnnotator.LOG.debug("DartFileResolver.resolveAbsolute: file not found: " + uri);
			}
		}
		return null;
	}
}
