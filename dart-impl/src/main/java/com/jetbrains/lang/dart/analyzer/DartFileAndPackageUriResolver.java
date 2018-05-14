package com.jetbrains.lang.dart.analyzer;

import static com.jetbrains.lang.dart.util.DartUrlResolver.FILE_SCHEME;
import static com.jetbrains.lang.dart.util.DartUrlResolver.PACKAGE_SCHEME;

import java.net.URI;

import javax.annotation.Nonnull;

import com.google.dart.engine.source.Source;
import com.google.dart.engine.source.UriKind;
import com.google.dart.engine.source.UriResolver;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.jetbrains.lang.dart.util.DartUrlResolver;

public class DartFileAndPackageUriResolver extends UriResolver
{

	private final
	@Nonnull
	Project myProject;
	private final
	@Nonnull
	DartUrlResolver myDartUrlResolver;

	public DartFileAndPackageUriResolver(final @Nonnull Project project, final @Nonnull DartUrlResolver dartUrlResolver)
	{
		myProject = project;
		myDartUrlResolver = dartUrlResolver;
	}

	public Source fromEncoding(final UriKind kind, final URI uri)
	{
		if(kind != UriKind.FILE_URI)
		{
			DartInProcessAnnotator.LOG.warn("DartFileUriResolver.fromEncoding: uri=" + uri + ", kind=" + kind);
		}

		return resolveAbsolute(uri);
	}

	public Source resolveAbsolute(final URI uri)
	{
		final String scheme = uri.getScheme();
		if(FILE_SCHEME.equals(scheme) || PACKAGE_SCHEME.equals(scheme))
		{
			final VirtualFile file = myDartUrlResolver.findFileByDartUrl(uri.toString());
			return file == null ? null : DartFileBasedSource.getSource(myProject, file);
		}

		return null;
	}
}
