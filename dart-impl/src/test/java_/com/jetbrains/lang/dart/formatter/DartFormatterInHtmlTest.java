package com.jetbrains.lang.dart.formatter;

import consulo.ide.impl.idea.openapi.util.io.FileUtil;
import consulo.language.codeStyle.CodeStyleSettings;
import consulo.language.codeStyle.CodeStyleSettingsManager;
import consulo.language.codeStyle.CommonCodeStyleSettings;
import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase;
import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.util.DartTestUtils;
import consulo.application.ApplicationManager;
import consulo.language.codeStyle.CodeStyleManager;
import consulo.project.Project;
import junit.framework.Assert;

import java.io.FileNotFoundException;
import java.io.FileWriter;

/**
 * @author: Fedor.Korotkov
 */
public abstract class DartFormatterInHtmlTest extends CodeInsightFixtureTestCase {
  protected CommonCodeStyleSettings myTestStyleSettings;


  @Override
  protected String getBasePath() {
    return FileUtil.toSystemDependentName(DartTestUtils.RELATIVE_TEST_DATA_PATH + "/formatter/html");
  }

  private void setTestStyleSettings() {
    Project project = getProject();
    CodeStyleSettings currSettings = CodeStyleSettingsManager.getSettings(project);
    Assert.assertNotNull(currSettings);
    CodeStyleSettings tempSettings = currSettings.clone();
    CodeStyleSettings.IndentOptions indentOptions = tempSettings.getIndentOptions(DartFileType.INSTANCE);
    indentOptions.INDENT_SIZE = 2;
    indentOptions.CONTINUATION_INDENT_SIZE = 2;
    indentOptions.TAB_SIZE = 2;
    Assert.assertNotNull(indentOptions);
    defineStyleSettings(tempSettings);
    CodeStyleSettingsManager.getInstance(project).setTemporarySettings(tempSettings);
  }

  protected void defineStyleSettings(CodeStyleSettings tempSettings) {
    myTestStyleSettings = tempSettings.getCommonSettings(DartLanguage.INSTANCE);
    myTestStyleSettings.KEEP_BLANK_LINES_IN_CODE = 2;
    myTestStyleSettings.METHOD_BRACE_STYLE = CommonCodeStyleSettings.END_OF_LINE;
    myTestStyleSettings.BRACE_STYLE = CommonCodeStyleSettings.END_OF_LINE;
    myTestStyleSettings.ALIGN_MULTILINE_PARAMETERS = false;
    myTestStyleSettings.ALIGN_MULTILINE_PARAMETERS_IN_CALLS = false;
    myTestStyleSettings.KEEP_FIRST_COLUMN_COMMENT = false;
  }

  private void doTest() throws Exception {
    myFixture.configureByFile(getTestName(false) + ".html");
    ApplicationManager.getApplication().runWriteAction(new Runnable() {
      @Override
      public void run() {
        CodeStyleManager.getInstance(myFixture.getProject()).reformat(myFixture.getFile());
      }
    });
    try {
      myFixture.checkResultByFile(getTestName(false) + ".txt");
    }
    catch (RuntimeException e) {
      if (!(e.getCause() instanceof FileNotFoundException)) {
        throw e;
      }
      final String path = getTestDataPath() + getTestName(false) + ".txt";
      FileWriter writer = new FileWriter(FileUtil.toSystemDependentName(path));
      try {
        writer.write(myFixture.getFile().getText().trim());
      }
      finally {
        writer.close();
      }
      fail("No output text found. File " + path + " created.");
    }
  }

  public void testDefault() throws Exception {
    doTest();
  }
}

