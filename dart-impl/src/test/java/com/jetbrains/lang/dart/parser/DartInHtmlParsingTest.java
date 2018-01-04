package com.jetbrains.lang.dart.parser;

import com.jetbrains.lang.dart.util.DartTestUtils;
import consulo.testFramework.ParsingTestCase;

/**
 * @author: Fedor.Korotkov
 */
public class DartInHtmlParsingTest extends ParsingTestCase
{
  public DartInHtmlParsingTest() {
    super("parsing/html", "html");
  }

  @Override
  protected void setUp() throws Exception {
/*    myLanguage = HTMLLanguage.INSTANCE;
    super.setUp();
    addExplicitExtension(LanguageParserDefinitions.INSTANCE, XMLLanguage.INSTANCE, new XMLParserDefinition());
    addExplicitExtension(LanguageParserDefinitions.INSTANCE, DTDLanguage.INSTANCE, new DTDParserDefinition());
    addExplicitExtension(LanguageParserDefinitions.INSTANCE, HTMLLanguage.INSTANCE, new HTMLParserDefinition());
    addExplicitExtension(LanguageASTFactory.INSTANCE, HTMLLanguage.INSTANCE, new XmlASTFactory());
    addExplicitExtension(LanguageASTFactory.INSTANCE, XMLLanguage.INSTANCE, new XmlASTFactory());
    addExplicitExtension(LanguageASTFactory.INSTANCE, DTDLanguage.INSTANCE, new XmlASTFactory());
    registerExtensionPoint(new ExtensionPointName<XmlChildRole.StartTagEndTokenProvider>("com.intellij.xml.startTagEndToken"),
                           XmlChildRole.StartTagEndTokenProvider.class);
    registerExtensionPoint(new ExtensionPointName<HtmlEmbeddedTokenTypesProvider>("com.intellij.html.embeddedTokenTypesProvider"),
                           HtmlEmbeddedTokenTypesProvider.class);
    registerExtensionPoint(new ExtensionPointName<HtmlInlineScriptTokenTypesProvider>("com.intellij.html.inlineScriptTokenTypesProvider"),
                           HtmlInlineScriptTokenTypesProvider.class);
    addExplicitExtension(LanguageHtmlScriptContentProvider.INSTANCE, DartLanguage.INSTANCE, new DartScriptContentProvider());
  */}

  @Override
  protected String getTestDataPath() {
    return DartTestUtils.BASE_TEST_DATA_PATH;
  }

  public void testHtml1() throws Throwable {
    doTest(true);
  }
}
