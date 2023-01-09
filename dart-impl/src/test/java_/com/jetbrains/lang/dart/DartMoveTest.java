package com.jetbrains.lang.dart;

import consulo.ide.impl.idea.openapi.util.io.FileUtil;
import consulo.language.editor.refactoring.move.fileOrDirectory.MoveFilesOrDirectoriesProcessor;
import consulo.language.psi.PsiDirectory;
import consulo.language.psi.PsiElement;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import consulo.language.psi.PsiUtilCore;
import com.intellij.testFramework.PlatformTestUtil;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import com.jetbrains.lang.dart.util.DartTestUtils;
import consulo.document.FileDocumentManager;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author: Fedor.Korotkov
 */
public abstract class DartMoveTest extends LightPlatformCodeInsightFixtureTestCase {
  @Override
  protected String getBasePath() {
    return consulo.ide.impl.idea.openapi.util.io.FileUtil.toSystemDependentName(DartTestUtils.RELATIVE_TEST_DATA_PATH + "/move/");
  }

  //Both names are relative to root directory
  private void doTest(String toMove, final String targetDirName) throws Exception {
    doTest(new String[]{toMove}, targetDirName);
  }

  //Both names are relative to root directory
  private void doTest(final String[] toMove, final String targetDirName) throws Exception {
    myFixture.copyDirectoryToProject(getTestName(true) + "/before", getTestName(true));
    Collection<PsiElement> files = new ArrayList<PsiElement>();
    for (String s : toMove) {
      final VirtualFile child = myFixture.findFileInTempDir(getTestName(true) + "/" + s);
      assertNotNull("Neither class nor file " + s + " not found", child);
      files.add(myFixture.getPsiManager().findFile(child));
    }
    final VirtualFile child1 = myFixture.findFileInTempDir(getTestName(true) + "/" + targetDirName);
    assertNotNull("Target dir " + targetDirName + " not found", child1);
    final PsiDirectory targetDirectory = myFixture.getPsiManager().findDirectory(child1);
    assertNotNull(targetDirectory);

    new MoveFilesOrDirectoriesProcessor(myFixture.getProject(), PsiUtilCore.toPsiElementArray(files), targetDirectory,
                                        false, true, null, null).run();
    FileDocumentManager.getInstance().saveAllDocuments();

    VirtualFile expected = LocalFileSystem.getInstance().findFileByPath(getTestDataPath() + getTestName(true) + "/after");
    PlatformTestUtil.assertDirectoriesEqual(expected, myFixture.findFileInTempDir(getTestName(true)));
  }

  public void testMoveFile2() throws Exception {
    doTest("bar/Foo.dart", "");
  }

  public void testMoveFile1() throws Exception {
    doTest("Foo.dart", "bar");
  }
}
