package com.jetbrains.lang.dart.ide.settings;

import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.psi.*;
import consulo.application.util.SystemInfo;
import consulo.application.util.function.Processor;
import consulo.content.bundle.Sdk;
import consulo.dart.module.extension.DartModuleExtension;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.util.dataholder.Key;
import consulo.util.io.FileUtil;
import consulo.util.lang.Pair;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.VirtualFileManager;
import consulo.virtualFileSystem.util.VirtualFileUtil;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.io.File;
import java.io.FileFilter;
import java.util.*;

/**
 * @author: Fedor.Korotkov
 */
public class DartSdkUtil {
  private static final Key<Pair<Long, Map<String, String>>> LIBRARIES_TIME_AND_MAP_KEY = Key.create("dart.internal.libraries");

  @Nullable
  public static VirtualFile getCompiler(@Nullable Sdk sdk) {
    if (sdk == null) {
      return null;
    }
    return VirtualFileManager.getInstance().findFileByUrl(getCompilerPath(sdk));
  }

  public static String getCompilerPath(@Nullable Sdk sdk) {
    if (sdk == null) {
      return null;
    }
    return VirtualFileUtil.pathToUrl(sdk.getHomePath()) + "/bin/dart";
  }

  @Nullable
  public static VirtualFile getAnalyzer(@Nullable Sdk sdk) {
    if (sdk == null) {
      return null;
    }
    return VirtualFileManager.getInstance().findFileByUrl(getAnalyzerPath(sdk));
  }

  public static String getAnalyzerPath(@Nullable Sdk sdk) {
    if (sdk == null) {
      return null;
    }
    return VirtualFileUtil.pathToUrl(sdk.getHomePath()) + "/bin/" + (SystemInfo.isWindows ? "dartanalyzer.bat" : "dartanalyzer");
  }

  @Nullable
  public static VirtualFile getDart2JS(@Nullable Sdk sdk) {
    if (sdk == null) {
      return null;
    }
    return VirtualFileManager.getInstance().findFileByUrl(getDart2JSPath(sdk));
  }

  public static String getDart2JSPath(@Nullable Sdk sdk) {
    if (sdk == null) {
      return null;
    }
    return VirtualFileUtil.pathToUrl(sdk.getHomePath()) + "/bin/" + (SystemInfo.isWindows ? "dart2js.bat" : "dart2js");
  }

  @Nullable
  public static VirtualFile getPub(@Nullable Sdk sdk) {
    if (sdk == null) {
      return null;
    }
    return VirtualFileManager.getInstance().findFileByUrl(getPubPath(sdk));
  }

  public static String getPubPath(@Nullable Sdk sdk) {
    if (sdk == null) {
      return null;
    }
    return VirtualFileUtil.pathToUrl(sdk.getHomePath()) + "/bin/" + (SystemInfo.isWindows ? "pub.bat" : "pub");
  }

  @Nullable
  public static VirtualFile findSdkLibrary(@Nullable Sdk sdk, PsiElement context, String libName) {
    if (sdk == null) {
      return null;
    }
    VirtualFile libRoot = getLib(sdk);
    String relativeLibPath = getLibrariesMap(sdk, context).get(libName);
    if (relativeLibPath == null) {
      return null;
    }
    return VirtualFileUtil.findRelativeFile(libRoot, relativeLibPath.split("/"));
  }

  public static Collection<String> getLibraries(@Nullable Sdk sdk, PsiElement context) {
    if (sdk == null) {
      return Collections.emptyList();
    }
    return getLibrariesMap(sdk, context).keySet();
  }

  @Nullable
  public static VirtualFile getLib(@Nullable Sdk sdk) {
    if (sdk == null) {
      return null;
    }
    return VirtualFileManager.getInstance().findFileByUrl(VirtualFileUtil.pathToUrl(getLibPath(sdk)));
  }

  public static String getLibPath(@Nullable Sdk sdk) {
    if (sdk == null) {
      return null;
    }
    return sdk.getHomePath() + "/lib/";
  }

  @Nonnull
  private static Map<String, String> getLibrariesMap(@Nullable Sdk sdk, @Nullable PsiElement context) {
    if (sdk == null) {
      return Collections.emptyMap();
    }
    VirtualFile configFile = getConfigFile(sdk);
    if (configFile == null || context == null) {
      return Collections.emptyMap();
    }

    Pair<Long, Map<String, String>> data = configFile.getUserData(LIBRARIES_TIME_AND_MAP_KEY);
    final Long cachedTimestamp = data == null ? null : data.first;
    long modificationCount = configFile.getModificationCount();
    if (cachedTimestamp == null || !cachedTimestamp.equals(modificationCount)) {
      PsiFile psiFile = context.getManager().findFile(configFile);
      data = Pair.create(modificationCount, computeData(psiFile));
      configFile.putUserData(LIBRARIES_TIME_AND_MAP_KEY, data);
    }
    return data.getSecond();
  }

  public static Map<String, String> computeData(@Nullable PsiFile file) {
    if (file == null) {
      return Collections.emptyMap();
    }
    final Map<String, String> result = new HashMap<String, String>();
    file.acceptChildren(new DartRecursiveVisitor() {
      @Override
      public void visitConstConstructorExpression(@Nonnull DartConstConstructorExpression constructorExpression) {
        Pair<String, String> libInfo = extractLibraryInfo(constructorExpression);
        if (libInfo != null) {
          result.put(libInfo.getFirst(), libInfo.getSecond());
        }
      }
    });
    return result;
  }

  @Nullable
  private static Pair<String, String> extractLibraryInfo(DartConstConstructorExpression constructorExpression) {
    PsiElement parent = constructorExpression.getParent();
    DartStringLiteralExpression literalExpression = PsiTreeUtil.getChildOfType(parent, DartStringLiteralExpression.class);
    if (literalExpression == null) {
      return null;
    }
    String libName = StringUtil.unquoteString(literalExpression.getText());

    DartArguments arguments = constructorExpression.getArguments();
    DartArgumentList argumentList = arguments != null ? arguments.getArgumentList() : null;
    List<DartExpression> expressionList = argumentList != null ? argumentList.getExpressionList() : null;

    String libPath = expressionList != null && !expressionList.isEmpty() ? expressionList.iterator().next().getText() : null;
    return Pair.create(libName, StringUtil.unquoteString(StringUtil.notNullize(libPath)));
  }

  @Nullable
  public static VirtualFile getConfigFile(@Nullable Sdk sdk) {
    String path = VirtualFileUtil.pathToUrl(sdk.getHomePath()) + "/lib/_internal/libraries.dart";
    return VirtualFileManager.getInstance().findFileByUrl(path);
  }

  @Nullable
  public static Sdk getSdkForModule(@Nullable Module module) {
    return module == null ? null : ModuleUtilCore.getSdk(module, DartModuleExtension.class);
  }

  private static List<File> findDartFiles(@Nonnull File rootDir) {
    final File libRoot = new File(rootDir, "lib");
    if (!libRoot.exists()) {
      return Collections.emptyList();
    }
    final List<File> result = new ArrayList<File>();
    final Processor<File> fileProcessor = new Processor<File>() {
      @Override
      public boolean process(File file) {
        if (file.isFile() && file.getName().endsWith("." + DartFileType.DEFAULT_EXTENSION)) {
          result.add(file);
        }
        return true;
      }
    };
    for (File child : libRoot.listFiles(new FileFilter() {
      @Override
      public boolean accept(File file) {
        return !"html".equals(file.getName()) && !"_internal".equals(file.getName());
      }
    })) {
      FileUtil.processFilesRecursively(child, fileProcessor);
    }

    File htmlDartium = new File(new File(libRoot, "html"), "dartium");
    if (htmlDartium.exists()) {
      FileUtil.processFilesRecursively(htmlDartium, fileProcessor);
    }

    return result;
  }
}
