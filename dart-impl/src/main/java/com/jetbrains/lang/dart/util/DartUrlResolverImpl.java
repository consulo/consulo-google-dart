package com.jetbrains.lang.dart.util;

import static com.jetbrains.lang.dart.util.PubspecYamlUtil.DEPENDENCIES;
import static com.jetbrains.lang.dart.util.PubspecYamlUtil.DEV_DEPENDENCIES;
import static com.jetbrains.lang.dart.util.PubspecYamlUtil.LIB_DIRECTORY_NAME;
import static com.jetbrains.lang.dart.util.PubspecYamlUtil.NAME;
import static com.jetbrains.lang.dart.util.PubspecYamlUtil.PATH;
import static com.jetbrains.lang.dart.util.PubspecYamlUtil.PUBSPEC_YAML;
import static com.jetbrains.lang.dart.util.PubspecYamlUtil.getPubspecYamlInfo;

import gnu.trove.THashMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.LibraryOrderEntry;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.roots.impl.libraries.LibraryEx;
import com.intellij.openapi.roots.impl.libraries.ProjectLibraryTable;
import com.intellij.openapi.roots.libraries.LibraryProperties;
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PairConsumer;
import com.jetbrains.lang.dart.ide.index.DartLibraryIndex;
import consulo.dart.module.extension.DartModuleExtension;
import com.jetbrains.lang.dart.sdk.DartConfigurable;
import com.jetbrains.lang.dart.sdk.listPackageDirs.DartListPackageDirsLibraryProperties;
import com.jetbrains.lang.dart.sdk.listPackageDirs.PubListPackageDirsAction;

class DartUrlResolverImpl extends DartUrlResolver
{

	private final
	@Nonnull
	Project myProject;
	private final
	@Nullable
	Sdk myDartSdk;
	private final
	@Nullable
	VirtualFile myPubspecYamlFile;
	private final
	@Nonnull
	List<VirtualFile> myPackageRoots = new ArrayList<VirtualFile>();
	private final
	@Nonnull
	Map<String, VirtualFile> myLivePackageNameToDirMap = new THashMap<String, VirtualFile>();
	private final
	@Nonnull
	Map<String, Set<String>> myPubListPackageDirsMap = new THashMap<String, Set<String>>();

	DartUrlResolverImpl(final @Nonnull Project project, final @Nonnull VirtualFile contextFile)
	{
		myProject = project;
		Module moduleForFile = ModuleUtilCore.findModuleForFile(contextFile, project);
		myDartSdk = moduleForFile == null ? null : ModuleUtilCore.getSdk(moduleForFile, DartModuleExtension.class);
		myPubspecYamlFile = initPackageRootsAndReturnPubspecYamlFile(contextFile);
		initLivePackageNameToDirMap();
		initPubListPackageDirsMap(contextFile);
	}

	@Override
	@Nullable
	public VirtualFile getPubspecYamlFile()
	{
		return myPubspecYamlFile;
	}

	@Override
	@Nonnull
	public VirtualFile[] getPackageRoots()
	{
		return myPackageRoots.toArray(new VirtualFile[myPackageRoots.size()]);
	}

	@Override
	public void processLivePackages(final @Nonnull PairConsumer<String, VirtualFile> packageNameAndDirConsumer)
	{
		for(Map.Entry<String, VirtualFile> entry : myLivePackageNameToDirMap.entrySet())
		{
			packageNameAndDirConsumer.consume(entry.getKey(), entry.getValue());
		}
	}

	@Override
	@Nullable
	public VirtualFile getPackageDirIfLivePackageOrFromPubListPackageDirs(final @Nonnull String packageName)
	{
		final VirtualFile dir = myLivePackageNameToDirMap.get(packageName);
		if(dir != null)
		{
			return dir;
		}

		final Set<String> dirPaths = myPubListPackageDirsMap.get(packageName);
		if(dirPaths != null)
		{
			for(String dirPath : dirPaths)
			{
				final VirtualFile packageDir = LocalFileSystem.getInstance().findFileByPath(dirPath);
				if(packageDir != null)
				{
					return packageDir;
				}
			}
		}

		return null;
	}

	@Override
	@Nullable
	public VirtualFile findFileByDartUrl(final @Nonnull String url)
	{
		if(url.startsWith(DART_PREFIX))
		{
			return findFileInDartSdkLibFolder(myProject, myDartSdk, url);
		}

		if(url.startsWith(PACKAGE_PREFIX))
		{
			final String packageRelPath = url.substring(PACKAGE_PREFIX.length());

			final int slashIndex = packageRelPath.indexOf('/');
			final String packageName = slashIndex > 0 ? packageRelPath.substring(0, slashIndex) : packageRelPath;
			final String relPathToPackageDir = slashIndex > 0 ? packageRelPath.substring(slashIndex + 1) : "";

			final VirtualFile packageDir = StringUtil.isEmpty(packageName) ? null : myLivePackageNameToDirMap.get(packageName);
			if(packageDir != null)
			{
				return packageDir.findFileByRelativePath(relPathToPackageDir);
			}

			for(final VirtualFile packageRoot : myPackageRoots)
			{
				final VirtualFile file = packageRoot.findFileByRelativePath(packageRelPath);
				if(file != null)
				{
					return file;
				}
			}

			final Set<String> packageDirs = myPubListPackageDirsMap.get(packageName);
			if(packageDirs != null)
			{
				for(String packageDirPath : packageDirs)
				{
					final VirtualFile file = LocalFileSystem.getInstance().findFileByPath(packageDirPath + "/" + relPathToPackageDir);
					if(file != null)
					{
						return file;
					}
				}
			}
		}

		if(url.startsWith(FILE_PREFIX))
		{
			final String path = StringUtil.trimLeading(url.substring(FILE_PREFIX.length()), '/');
			return LocalFileSystem.getInstance().findFileByPath(SystemInfo.isWindows ? path : ("/" + path));
		}

		return null;
	}

	@Override
	@Nonnull
	public String getDartUrlForFile(final @Nonnull VirtualFile file)
	{
		String result = null;

		if(myDartSdk != null)
		{
			result = getUrlIfFileFromSdkLib(myProject, file, myDartSdk);
		}
		if(result != null)
		{
			return result;
		}

		result = getUrlIfFileFromLivePackage(file, myLivePackageNameToDirMap);
		if(result != null)
		{
			return result;
		}

		result = getUrlIfFileFromPackageRoot(file, myPackageRoots);
		if(result != null)
		{
			return result;
		}

		result = getUrlIfFileFromPubListPackageDirs(myProject, file, myPubListPackageDirsMap);
		if(result != null)
		{
			return result;
		}

		// see com.google.dart.tools.debug.core.server.ServerBreakpointManager#getAbsoluteUrlForResource()
		return new File(file.getPath()).toURI().toString();
	}

	@Nullable
	private static String getUrlIfFileFromSdkLib(final @Nonnull Project project, final @Nonnull VirtualFile file, final @Nonnull Sdk sdk)
	{
		final VirtualFile sdkLibFolder = LocalFileSystem.getInstance().findFileByPath(sdk.getHomePath() + "/lib");
		final String relativeToSdkLibFolder = sdkLibFolder == null ? null : VfsUtilCore.getRelativePath(file, sdkLibFolder, '/');
		final String sdkLibName = relativeToSdkLibFolder == null ? null : DartLibraryIndex.getStandardLibraryNameByRelativePath(project,
				sdk,  relativeToSdkLibFolder);
		return sdkLibName != null ? DART_PREFIX + sdkLibName : relativeToSdkLibFolder != null ? DART_PREFIX + relativeToSdkLibFolder : null;
	}

	@Nullable
	private static String getUrlIfFileFromLivePackage(final @Nonnull VirtualFile file, final @Nonnull Map<String,
			VirtualFile> livePackageNameToDirMap)
	{
		for(Map.Entry<String, VirtualFile> entry : livePackageNameToDirMap.entrySet())
		{
			final String packageName = entry.getKey();
			final VirtualFile packageDir = entry.getValue();
			final String relPath = VfsUtilCore.getRelativePath(file, packageDir, '/');
			if(relPath != null)
			{
				return PACKAGE_PREFIX + packageName + "/" + relPath;
			}
		}
		return null;
	}

	@Nullable
	private static String getUrlIfFileFromPackageRoot(final @Nonnull VirtualFile file, final @Nonnull List<VirtualFile> packageRoots)
	{
		for(VirtualFile packageRoot : packageRoots)
		{
			final String relPath = VfsUtilCore.getRelativePath(file, packageRoot, '/');
			if(relPath != null)
			{
				return PACKAGE_PREFIX + relPath;
			}
		}
		return null;
	}

	@Nullable
	private static String getUrlIfFileFromPubListPackageDirs(final @Nonnull Project project, final @Nonnull VirtualFile file,
			final @Nonnull Map<String, Set<String>> pubListPackageDirsMap)
	{
		final String filePath = file.getPath();

		for(OrderEntry orderEntry : ProjectRootManager.getInstance(project).getFileIndex().getOrderEntriesForFile(file))
		{
			if(orderEntry instanceof LibraryOrderEntry &&
					LibraryTablesRegistrar.PROJECT_LEVEL.equals(((LibraryOrderEntry) orderEntry).getLibraryLevel()) &&
					PubListPackageDirsAction.PUB_LIST_PACKAGE_DIRS_LIB_NAME.equals(((LibraryOrderEntry) orderEntry).getLibraryName()))
			{
				for(Map.Entry<String, Set<String>> mapEntry : pubListPackageDirsMap.entrySet())
				{
					for(String dirPath : mapEntry.getValue())
					{
						if(filePath.startsWith(dirPath + "/"))
						{
							final String packageName = mapEntry.getKey();
							return PACKAGE_PREFIX + packageName + filePath.substring(dirPath.length());
						}
					}
				}
				return null;
			}
		}
		return null;
	}

	@Nullable
	private VirtualFile initPackageRootsAndReturnPubspecYamlFile(final @Nonnull VirtualFile contextFile)
	{
		final Module module = ModuleUtilCore.findModuleForFile(contextFile, myProject);
		if(module == null)
		{
			return null;
		}

		final VirtualFile[] customPackageRoots = DartConfigurable.getCustomPackageRoots(module);
		if(customPackageRoots.length > 0)
		{
			Collections.addAll(myPackageRoots, customPackageRoots);
			return null;
		}

		final VirtualFile pubspecYamlFile = findPubspecYamlFile(myProject, contextFile);
		final VirtualFile parentFolder = pubspecYamlFile == null ? null : pubspecYamlFile.getParent();
		final VirtualFile packagesFolder = parentFolder == null ? null : parentFolder.findChild(PACKAGES_FOLDER_NAME);
		if(packagesFolder != null && packagesFolder.isDirectory())
		{
			myPackageRoots.add(packagesFolder);
		}

		return pubspecYamlFile;
	}

	private static VirtualFile findPubspecYamlFile(final @Nonnull Project project, final @Nonnull VirtualFile contextFile)
	{
		final ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();
		VirtualFile parent = contextFile;
		while((parent = parent.getParent()) != null && fileIndex.isInContent(parent))
		{
			final VirtualFile file = parent.findChild(PUBSPEC_YAML);
			if(file != null && !file.isDirectory())
			{
				return file;
			}
		}

		return null;
	}

	private void initLivePackageNameToDirMap()
	{
		if(myPubspecYamlFile == null)
		{
			return;
		}

		final VirtualFile baseDir = myPubspecYamlFile.getParent();
		final Map<String, Object> yamlInfo = getPubspecYamlInfo(myPubspecYamlFile);
		if(baseDir != null && yamlInfo != null)
		{
			fillLivePackageNameToDirMap(myProject, myLivePackageNameToDirMap, baseDir, yamlInfo);
		}
	}

	private static void fillLivePackageNameToDirMap(final @Nonnull Project project, final @Nonnull Map<String, VirtualFile> packageNameToDirMap,
			final @Nonnull VirtualFile baseDir, final @Nonnull Map<String, Object> yamlInfo)
	{
		final Object name = yamlInfo.get(NAME);
		final VirtualFile libFolder = baseDir.findChild(LIB_DIRECTORY_NAME);
		if(name instanceof String && libFolder != null && libFolder.isDirectory())
		{
			packageNameToDirMap.put((String) name, libFolder);
		}

		addPathPackagesToMap(project, packageNameToDirMap, yamlInfo.get(DEPENDENCIES), baseDir);
		addPathPackagesToMap(project, packageNameToDirMap, yamlInfo.get(DEV_DEPENDENCIES), baseDir);
	}

	// Path packages: https://www.dartlang.org/tools/pub/dependencies.html#path-packages
	private static void addPathPackagesToMap(final @Nonnull Project project, final @Nonnull Map<String, VirtualFile> packageNameToDirMap,
			final @Nullable Object yamlDep, final @Nonnull VirtualFile baseDir)
	{
		// see com.google.dart.tools.core.pub.PubspecModel#processDependencies
		if(!(yamlDep instanceof Map))
		{
			return;
		}

		final ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();
		//noinspection unchecked
		for(Map.Entry<String, Object> packageEntry : ((Map<String, Object>) yamlDep).entrySet())
		{
			final String packageName = packageEntry.getKey();

			final Object packageEntryValue = packageEntry.getValue();
			if(packageEntryValue instanceof Map)
			{
				final Object pathObj = ((Map) packageEntryValue).get(PATH);
				if(pathObj instanceof String)
				{
					final VirtualFile packageFolder = VfsUtilCore.findRelativeFile(pathObj + "/" + LIB_DIRECTORY_NAME, baseDir);
					if(packageFolder != null && packageFolder.isDirectory() && fileIndex.isInContent(packageFolder))
					{
						packageNameToDirMap.put(packageName, packageFolder);
					}
				}
			}
		}
	}

	private void initPubListPackageDirsMap(final @Nonnull VirtualFile contextFile)
	{
		final Module module = ModuleUtilCore.findModuleForFile(contextFile, myProject);

		final List<OrderEntry> orderEntries = module != null ? Arrays.asList(ModuleRootManager.getInstance(module).getOrderEntries()) :
				ProjectRootManager.getInstance(myProject).getFileIndex().getOrderEntriesForFile(contextFile);
		for(OrderEntry orderEntry : orderEntries)
		{
			if(orderEntry instanceof LibraryOrderEntry &&
					LibraryTablesRegistrar.PROJECT_LEVEL.equals(((LibraryOrderEntry) orderEntry).getLibraryLevel()) &&
					PubListPackageDirsAction.PUB_LIST_PACKAGE_DIRS_LIB_NAME.equals(((LibraryOrderEntry) orderEntry).getLibraryName()))
			{
				final LibraryEx library = (LibraryEx) ProjectLibraryTable.getInstance(myProject).getLibraryByName(PubListPackageDirsAction
						.PUB_LIST_PACKAGE_DIRS_LIB_NAME);
				final LibraryProperties properties = library == null ? null : library.getProperties();

				if(properties instanceof DartListPackageDirsLibraryProperties)
				{
					myPubListPackageDirsMap.putAll(((DartListPackageDirsLibraryProperties) properties).getPackageNameToDirsMap());
					return;
				}
			}
		}
	}
}
