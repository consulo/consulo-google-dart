package com.jetbrains.lang.dart.ide;

import consulo.application.ApplicationManager;
import consulo.codeEditor.Editor;
import consulo.ide.impl.idea.codeInsight.lookup.impl.LookupImpl;
import consulo.ide.impl.idea.codeInsight.template.impl.TemplateManagerImpl;
import consulo.language.codeStyle.CodeStyleManager;
import consulo.language.editor.template.TemplateState;
import consulo.ide.impl.idea.codeInsight.template.impl.actions.ListTemplatesAction;
import consulo.ide.impl.idea.openapi.util.Disposer;
import consulo.ide.impl.idea.openapi.util.io.FileUtil;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import com.jetbrains.lang.dart.util.DartTestUtils;
import consulo.language.editor.completion.lookup.Lookup;
import consulo.language.editor.completion.lookup.LookupManager;
import consulo.project.Project;

/**
 * @author: Fedor.Korotkov
 */
public abstract class DartLiveTemplatesTest extends LightPlatformCodeInsightFixtureTestCase {
  @Override
  protected String getBasePath() {
    return FileUtil.toSystemDependentName(DartTestUtils.RELATIVE_TEST_DATA_PATH + "/liveTemplates/");
  }

  public static void expandTemplate(final Editor editor) {
    final Project project = editor.getProject();
    assertNotNull(project);
    new ListTemplatesAction().actionPerformedImpl(project, editor);
    final LookupImpl lookup = (consulo.ide.impl.idea.codeInsight.lookup.impl.LookupImpl)LookupManager.getActiveLookup(editor);
    assertNotNull(lookup);
    lookup.finishLookup(Lookup.NORMAL_SELECT_CHAR);
    TemplateState template = consulo.ide.impl.idea.codeInsight.template.impl.TemplateManagerImpl.getTemplateState(editor);
    if (template != null) {
      Disposer.dispose(template);
    }
  }

  private void doTest() throws Exception {
    doTest(getTestName(false) + ".dart");
  }

  private void doTest(String... files) throws Exception {
    myFixture.configureByFiles(files);
    expandTemplate(myFixture.getEditor());
    ApplicationManager.getApplication().runWriteAction(new Runnable() {
      @Override
      public void run() {
        CodeStyleManager.getInstance(myFixture.getProject()).reformat(myFixture.getFile());
      }
    });
    myFixture.getEditor().getSelectionModel().removeSelection();
    myFixture.checkResultByFile(getTestName(false) + ".after.dart");
  }

  public void testItar1() throws Throwable {
    doTest();
  }

  public void testItar2() throws Throwable {
    doTest();
  }

  public void testIter() throws Throwable {
    doTest();
  }
}
