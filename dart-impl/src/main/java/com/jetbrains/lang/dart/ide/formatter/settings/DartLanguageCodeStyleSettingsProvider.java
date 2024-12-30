package com.jetbrains.lang.dart.ide.formatter.settings;

import com.jetbrains.lang.dart.DartLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.codeStyle.CommonCodeStyleSettings;
import consulo.language.codeStyle.setting.CodeStyleSettingsCustomizable;
import consulo.language.codeStyle.setting.IndentOptionsEditor;
import consulo.language.codeStyle.setting.LanguageCodeStyleSettingsProvider;
import consulo.language.codeStyle.ui.setting.SmartIndentOptionsEditor;

import jakarta.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
@ExtensionImpl
public class DartLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {

  @Nonnull
  @Override
  public Language getLanguage() {
    return DartLanguage.INSTANCE;
  }

  @Override
  public String getCodeSample(@Nonnull SettingsType settingsType) {
    if (settingsType == SettingsType.SPACING_SETTINGS) {
      return SPACING_CODE_SAMPLE;
    }
    if (settingsType == SettingsType.WRAPPING_AND_BRACES_SETTINGS) {
      return WRAPPING_CODE_SAMPLE;
    }
    if (settingsType == SettingsType.INDENT_SETTINGS) {
      return INDENT_CODE_SAMPLE;
    }
    return BLANK_LINES_CODE_SAMPLE;
  }

  @Override
  public IndentOptionsEditor getIndentOptionsEditor() {
    return new SmartIndentOptionsEditor();
  }

  @Override
  public CommonCodeStyleSettings getDefaultCommonSettings() {
    CommonCodeStyleSettings defaultSettings = new CommonCodeStyleSettings(getLanguage());
    CommonCodeStyleSettings.IndentOptions indentOptions = defaultSettings.initIndentOptions();
    indentOptions.INDENT_SIZE = 2;
    indentOptions.CONTINUATION_INDENT_SIZE = 4;
    indentOptions.TAB_SIZE = 2;

    defaultSettings.KEEP_LINE_BREAKS = false;

    return defaultSettings;
  }

  @Override
  public void customizeSettings(@Nonnull CodeStyleSettingsCustomizable consumer, @Nonnull SettingsType settingsType) {
    if (settingsType == SettingsType.SPACING_SETTINGS) {
      consumer.showStandardOptions("SPACE_BEFORE_METHOD_CALL_PARENTHESES",
                                   "SPACE_BEFORE_METHOD_PARENTHESES",
                                   "SPACE_BEFORE_IF_PARENTHESES",
                                   "SPACE_BEFORE_WHILE_PARENTHESES",
                                   "SPACE_BEFORE_FOR_PARENTHESES",
                                   "SPACE_BEFORE_CATCH_PARENTHESES",
                                   "SPACE_BEFORE_SWITCH_PARENTHESES",
                                   "SPACE_AROUND_ASSIGNMENT_OPERATORS",
                                   "SPACE_AROUND_LOGICAL_OPERATORS",
                                   "SPACE_AROUND_EQUALITY_OPERATORS",
                                   "SPACE_AROUND_RELATIONAL_OPERATORS",
                                   "SPACE_AROUND_ADDITIVE_OPERATORS",
                                   "SPACE_AROUND_MULTIPLICATIVE_OPERATORS",
                                   "SPACE_AROUND_SHIFT_OPERATORS",
                                   "SPACE_BEFORE_METHOD_LBRACE",
                                   "SPACE_BEFORE_IF_LBRACE",
                                   "SPACE_BEFORE_ELSE_LBRACE",
                                   "SPACE_BEFORE_WHILE_LBRACE",
                                   "SPACE_BEFORE_FOR_LBRACE",
                                   "SPACE_BEFORE_SWITCH_LBRACE",
                                   "SPACE_BEFORE_TRY_LBRACE",
                                   "SPACE_BEFORE_CATCH_LBRACE",
                                   "SPACE_BEFORE_WHILE_KEYWORD",
                                   "SPACE_BEFORE_ELSE_KEYWORD",
                                   "SPACE_BEFORE_CATCH_KEYWORD",
                                   "SPACE_WITHIN_METHOD_CALL_PARENTHESES",
                                   "SPACE_WITHIN_METHOD_PARENTHESES",
                                   "SPACE_WITHIN_IF_PARENTHESES",
                                   "SPACE_WITHIN_WHILE_PARENTHESES",
                                   "SPACE_WITHIN_FOR_PARENTHESES",
                                   "SPACE_WITHIN_CATCH_PARENTHESES",
                                   "SPACE_WITHIN_SWITCH_PARENTHESES",
                                   "SPACE_BEFORE_QUEST",
                                   "SPACE_AFTER_QUEST",
                                   "SPACE_BEFORE_COLON",
                                   "SPACE_AFTER_COLON",
                                   "SPACE_AFTER_COMMA",
                                   "SPACE_AFTER_COMMA_IN_TYPE_ARGUMENTS",
                                   "SPACE_BEFORE_COMMA",
                                   "SPACE_AROUND_UNARY_OPERATOR"
      );
    }
    else if (settingsType == SettingsType.BLANK_LINES_SETTINGS) {
      consumer.showStandardOptions("KEEP_BLANK_LINES_IN_CODE");
    }
    else if (settingsType == SettingsType.WRAPPING_AND_BRACES_SETTINGS) {
      consumer.showStandardOptions("KEEP_LINE_BREAKS",
                                   "KEEP_FIRST_COLUMN_COMMENT",
                                   "BRACE_STYLE",
                                   "METHOD_BRACE_STYLE",
                                   "CALL_PARAMETERS_WRAP",
                                   "CALL_PARAMETERS_LPAREN_ON_NEXT_LINE",
                                   "CALL_PARAMETERS_RPAREN_ON_NEXT_LINE",
                                   "METHOD_PARAMETERS_WRAP",
                                   "METHOD_PARAMETERS_LPAREN_ON_NEXT_LINE",
                                   "METHOD_PARAMETERS_RPAREN_ON_NEXT_LINE",
                                   "ELSE_ON_NEW_LINE",
                                   "WHILE_ON_NEW_LINE",
                                   "CATCH_ON_NEW_LINE",
                                   "ALIGN_MULTILINE_PARAMETERS",
                                   "ALIGN_MULTILINE_PARAMETERS_IN_CALLS",
                                   "ALIGN_MULTILINE_BINARY_OPERATION",
                                   "BINARY_OPERATION_WRAP",
                                   "BINARY_OPERATION_SIGN_ON_NEXT_LINE",
                                   "TERNARY_OPERATION_WRAP",
                                   "TERNARY_OPERATION_SIGNS_ON_NEXT_LINE",
                                   "PARENTHESES_EXPRESSION_LPAREN_WRAP",
                                   "PARENTHESES_EXPRESSION_RPAREN_WRAP",
                                   "ALIGN_MULTILINE_TERNARY_OPERATION",
                                   "SPECIAL_ELSE_IF_TREATMENT");
    }
  }

  public static final String SPACING_CODE_SAMPLE = "class Foo {\n" +
    "    Array<Array<Int>> tmp;\n" +
    "    function foo(int x, z) {\n" +
    "        new Foo(x, 2);\n" +
    "        int absSum(int a, int b) {\n" +
    "            int value = a + b;\n" +
    "            return value > 0 ? value : -value;\n" +
    "        }\n" +
    "        var arr = [\"zero\", \"one\"];\n" +
    "        var y = (x ^ 0x123) << 2;\n" +
    "        for (i in tmp) {\n" +
    "            y = (y ^ 0x123) << 2;\n" +
    "        }\n" +
    "        var k = x % 2 == 1 ? 0 : 1;\n" +
    "        do {\n" +
    "            try {\n" +
    "                if (0 < x && x < 10) {\n" +
    "                    while (x != y) {\n" +
    "                        x = absSum(x * 3, 5);\n" +
    "                    }\n" +
    "                    z += 2;\n" +
    "                } else if (x > 20) {\n" +
    "                    z = x << 1;\n" +
    "                } else {\n" +
    "                    z = x | 2;\n" +
    "                }\n" +
    "                switch (k) {\n" +
    "                    case 0:\n" +
    "                        var s1 = 'zero';\n" +
    "                    case 2:\n" +
    "                        var s1 = 'two';\n" +
    "                    default:\n" +
    "                        var s1 = 'other';\n" +
    "                }\n" +
    "            } catch (string e) {\n" +
    "                var message = arr[0];\n" +
    "            }\n" +
    "        } while (x < 0);\n" +
    "    }\n" +
    "\n" +
    "    Foo(int n, int m) {\n" +
    "        tmp = new Array<Array<Int>>();\n" +
    "        for (int i; i < 10; ++i) tmp.push(new Array<Int>());\n" +
    "    }\n" +
    "}";

  public static final String WRAPPING_CODE_SAMPLE = "class Foo {\n" +
    "    // function fBar (x,y);\n" +
    "    fOne(argA, argB, argC, argD, argE, argF, argG, argH) {\n" +
    "        Array<string> numbers   = ['one', 'two', 'three', 'four', 'five', 'six'];\n" +
    "        var x = (\"\" + argA) + argB + argC + argD + argE + argF + argG + argH;\n" +
    "        try {\n" +
    "            this.fTwo(argA, argB, argC, this.fThree(\"\", argE, argF, argG, argH));\n" +
    "        } catch (string ignored) {}\n" +
    "        var z = argA == 'Some string' ? 'yes' : 'no';\n" +
    "        var colors = ['red', 'green', 'blue', 'black', 'white', 'gray'];\n" +
    "        for (colorIndex in colors) {\n" +
    "            var colorString = numbers[colorIndex];\n" +
    "        }\n" +
    "        do {\n" +
    "            colors.pop();\n" +
    "        } while (colors.length > 0);\n" +
    "    }\n" +
    "\n" +
    "    fTwo(strA, strB, strC, strD) {\n" +
    "        if (true)\n" +
    "        return strC;\n" +
    "        if (strA == 'one' ||\n" +
    "        strB == 'two') {\n" +
    "            return strA + strB;\n" +
    "        } else if (true) return strD;\n" +
    "        throw strD;\n" +
    "    }\n" +
    "\n" +
    "    fThree(strA, strB, strC, strD, strE) {\n" +
    "        return strA + strB + strC + strD + strE;\n" +
    "    }\n" +
    "}";

  public static final String BLANK_LINES_CODE_SAMPLE = "class Foo {\n" +
    "    Foo() {\n" +
    "    }\n" +
    "\n" +
    "\n" +
    "    main() {\n" +
    "        print(\"Hello!\");\n" +
    "    }\n" +
    "}";
  public static final String INDENT_CODE_SAMPLE = "main(){\n" +
    "  print(239);\n" +
    "}";
}
