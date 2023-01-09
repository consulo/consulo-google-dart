package com.jetbrains.lang.dart.completion.base;

import consulo.language.editor.completion.CompletionType;
import consulo.ide.impl.idea.openapi.util.io.FileUtil;
import consulo.util.lang.CharFilter;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase;
import com.jetbrains.lang.dart.util.DartTestUtils;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * @author: Fedor.Korotkov
 */
abstract public class DartCompletionTestBase extends CodeInsightFixtureTestCase {
  enum CheckType {EQUALS, INCLUDES, EXCLUDES}

  private final String myPath;

  @SuppressWarnings("JUnitTestCaseWithNonTrivialConstructors")
  public DartCompletionTestBase(String... path) {
    myPath = getPath(path);
  }

  private static String getPath(String... args) {
    final StringBuilder result = new StringBuilder();
    for (String folder : args) {
      if (result.length() > 0) {
        result.append("/");
      }
      result.append(folder);
    }
    return result.toString();
  }

  @Override
  protected String getBasePath() {
    return DartTestUtils.RELATIVE_TEST_DATA_PATH + "/" + myPath;
  }

  protected void doTest() throws Throwable {
    doTest(getTestName(false) + ".dart");
  }

  protected void doTest(char charToType) throws Throwable {
    configure(getTestName(false) + ".dart");
    myFixture.type(charToType);
    myFixture.checkResultByFile(getTestName(false) + ".txt");
  }

  protected void doTest(String... files) throws Throwable {
    configure(files);
    doTestVariantsInner(getTestName(false) + ".txt");
  }

  protected void configure(String... files) throws IOException {
    myFixture.configureByFiles(files);
  }

  protected void doTestVariantsInner(String fileName) throws Throwable {
    final VirtualFile virtualFile = myFixture.copyFileToProject(fileName);
    final Scanner in = new Scanner(virtualFile.getInputStream());

    final CompletionType type = CompletionType.valueOf(in.next());
    final int count = in.nextInt();
    final CheckType checkType = CheckType.valueOf(in.next());

    final List<String> variants = new ArrayList<String>();
    while (in.hasNext()) {
      final String variant = StringUtil.strip(in.next(), CharFilter.NOT_WHITESPACE_FILTER);
      if (variant.length() > 0) {
        variants.add(variant);
      }
    }

    myFixture.complete(type, count);
    List<String> stringList = myFixture.getLookupElementStrings();
    if (stringList == null) {
      stringList = Collections.emptyList();
    }

    if (checkType == CheckType.EQUALS) {
      UsefulTestCase.assertSameElements(stringList, variants);
    }
    else if (checkType == CheckType.INCLUDES) {
      variants.removeAll(stringList);
      assertTrue("Missing variants: " + variants, variants.isEmpty());
    }
    else if (checkType == CheckType.EXCLUDES) {
      variants.retainAll(stringList);
      assertTrue("Unexpected variants: " + variants, variants.isEmpty());
    }
  }
}
