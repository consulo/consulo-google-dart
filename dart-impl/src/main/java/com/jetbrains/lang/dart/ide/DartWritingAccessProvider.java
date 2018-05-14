package com.jetbrains.lang.dart.ide;

import static com.jetbrains.lang.dart.util.PubspecYamlUtil.PUBSPEC_YAML;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nonnull;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.WritingAccessProvider;
import com.intellij.psi.PsiFile;
import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.util.DartUrlResolver;

public class DartWritingAccessProvider extends WritingAccessProvider
{

	private final Project myProject;

	public DartWritingAccessProvider(Project project)
	{
		myProject = project;
	}

	@Nonnull
	@Override
	public Collection<VirtualFile> requestWriting(VirtualFile... files)
	{
		return Collections.emptyList();
	}

	@Override
	public boolean isPotentiallyWritable(@Nonnull VirtualFile file)
	{
		if(DartFileType.INSTANCE != file.getFileType())
		{
			return true;
		}
		return !isInDartSdkOrDartPackagesFolder(myProject, file);
	}

	public static boolean isInDartSdkOrDartPackagesFolder(final @Nonnull PsiFile psiFile)
	{
		final VirtualFile vFile = psiFile.getOriginalFile().getVirtualFile();
		return vFile != null && isInDartSdkOrDartPackagesFolder(psiFile.getProject(), vFile);
	}

	public static boolean isInDartSdkOrDartPackagesFolder(final @Nonnull Project project, final @Nonnull VirtualFile file)
	{
		final ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();

		if(fileIndex.isInLibraryClasses(file))
		{
			return true; // file in SDK or in custom package root
		}

		if(fileIndex.isInContent(file) && isInDartPackagesFolder(fileIndex, file))
		{
			return true; // symlinked child of 'packages' folder. Real location is in user cache folder for Dart packages, not in project
		}

		return false;
	}

	private static boolean isInDartPackagesFolder(final ProjectFileIndex fileIndex, final VirtualFile file)
	{
		VirtualFile parent = file;
		while((parent = parent.getParent()) != null && fileIndex.isInContent(parent))
		{
			if(DartUrlResolver.PACKAGES_FOLDER_NAME.equals(parent.getName()))
			{
				return VfsUtilCore.findRelativeFile("../" + PUBSPEC_YAML, parent) != null;
			}
		}

		return false;
	}
}
