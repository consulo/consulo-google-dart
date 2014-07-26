package com.jetbrains.lang.dart.ide.runner;

import static com.jetbrains.lang.dart.util.DartUrlResolver.DART_PREFIX;
import static com.jetbrains.lang.dart.util.DartUrlResolver.PACKAGE_PREFIX;
import static com.jetbrains.lang.dart.util.PubspecYamlUtil.PUBSPEC_YAML;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.execution.filters.Filter;
import com.intellij.execution.filters.OpenFileHyperlinkInfo;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.jetbrains.lang.dart.util.DartUrlResolver;

public class DartConsoleFilter implements Filter
{

	private final
	@NotNull
	Project myProject;
	private final
	@Nullable
	Sdk mySdk;
	private final
	@Nullable
	DartUrlResolver myDartUrlResolver;
	private Collection<VirtualFile> myAllPubspecYamlFiles;

	public DartConsoleFilter(final @NotNull Project project)
	{
		this(project, null, null);
	}

	public DartConsoleFilter(final @NotNull Project project, final @Nullable Sdk sdk, final @Nullable VirtualFile contextFile)
	{
		myProject = project;
		mySdk = sdk;
		myDartUrlResolver = contextFile == null ? null : DartUrlResolver.getInstance(project, contextFile);
	}

	@Override
	@Nullable
	public Result applyFilter(final String line, final int entireLength)
	{
		final DartPositionInfo info = DartPositionInfo.parsePositionInfo(line);
		if(info == null)
		{
			return null;
		}

		final VirtualFile file;
		switch(info.type)
		{
			case FILE:
				file = LocalFileSystem.getInstance().findFileByPath(info.path);
				break;
			case DART:
				file = DartUrlResolver.findFileInDartSdkLibFolder(myProject, mySdk, DART_PREFIX + info.path);
				break;
			case PACKAGE:
				if(myDartUrlResolver != null)
				{
					file = myDartUrlResolver.findFileByDartUrl(PACKAGE_PREFIX + info.path);
				}
				else
				{
					if(myAllPubspecYamlFiles == null)
					{
						myAllPubspecYamlFiles = FilenameIndex.getVirtualFilesByName(myProject, PUBSPEC_YAML,
								GlobalSearchScope.projectScope(myProject));
					}

					VirtualFile inPackage = null;
					for(VirtualFile yamlFile : myAllPubspecYamlFiles)
					{
						inPackage = DartUrlResolver.getInstance(myProject, yamlFile).findFileByDartUrl(PACKAGE_PREFIX + info.path);
						if(inPackage != null)
						{
							break;
						}
					}
					file = inPackage;
				}
				break;
			default:
				file = null;
		}

		if(file != null && !file.isDirectory())
		{
			final int highlightStartOffset = entireLength - line.length() + info.highlightingStartIndex;
			final int highlightEndOffset = entireLength - line.length() + info.highlightingEndIndex;
			return new Result(highlightStartOffset, highlightEndOffset, new OpenFileHyperlinkInfo(myProject, file, info.line, info.column));
		}

		return null;
	}
}
