package com.jetbrains.lang.dart.highlight;

import consulo.annotation.component.ExtensionImpl;
import consulo.colorScheme.TextAttributesKey;
import consulo.colorScheme.setting.AttributesDescriptor;
import consulo.google.dart.localize.DartLocalize;
import consulo.language.editor.colorScheme.setting.ColorSettingsPage;
import consulo.language.editor.highlight.SyntaxHighlighter;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;

import static com.jetbrains.lang.dart.highlight.DartSyntaxHighlighterColors.*;

@ExtensionImpl
public class DartColorsAndFontsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] ATTRS;

    private static final Map<String, TextAttributesKey> ourTags = new HashMap<>();

    static {
        ATTRS = new AttributesDescriptor[]{
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionLineComment(), LINE_COMMENT),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionBlockComment(), BLOCK_COMMENT),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionDocComment(), DOC_COMMENT),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionKeyword(), KEYWORD),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionNumber(), NUMBER),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionString(), STRING),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionValidStringEscape(), VALID_STRING_ESCAPE),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionInvalidStringEscape(), INVALID_STRING_ESCAPE),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionOperator(), OPERATION_SIGN),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionParenths(), PARENTHS),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionBrackets(), BRACKETS),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionBraces(), BRACES),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionComma(), COMMA),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionDot(), DOT),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionSemicolon(), SEMICOLON),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionBadCharacter(), BAD_CHARACTER),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionParameter(), PARAMETER),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionLocalFunction(), FUNCTION),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionLocalVariable(), LOCAL_VARIABLE),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionLocalVariableAccess(), LOCAL_VARIABLE_ACCESS),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionLabel(), LABEL),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionClass(), CLASS),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionMetadata(), METADATA),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionBuiltin(), BUILTIN),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionInstanceMemberFunction(), INSTANCE_MEMBER_FUNCTION),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionInstanceMemberFunctionCall(),
                INSTANCE_MEMBER_FUNCTION_CALL),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionInstanceMemberInheritedFunctionCall(),
                INHERITED_MEMBER_FUNCTION_CALL),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionInstanceMemberAbstractFunctionCall(),
                ABSTRACT_MEMBER_FUNCTION_CALL),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionStaticMemberFunction(), STATIC_MEMBER_FUNCTION),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionStaticMemberFunctionCall(),
                STATIC_MEMBER_FUNCTION_CALL),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionToplevelFunction(), TOP_LEVEL_FUNCTION_DECLARATION),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionToplevelFunctionCall(), TOP_LEVEL_FUNCTION_CALL),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionToplevelVariable(), TOP_LEVEL_VARIABLE_DECLARATION),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionToplevelVariableAccess(), TOP_LEVEL_VARIABLE_ACCESS),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionInstanceMemberVariable(), INSTANCE_MEMBER_VARIABLE),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionInstanceMemberVariableAccess(),
                INSTANCE_MEMBER_VARIABLE_ACCESS),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionStaticMemberVariable(), STATIC_MEMBER_VARIABLE),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionStaticMemberVariableAccess(),
                STATIC_MEMBER_VARIABLE_ACCESS),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionConstructorCall(), CONSTRUCTOR_CALL),
            new AttributesDescriptor(DartLocalize.dartColorSettingsDescriptionConstructorDecl(), CONSTRUCTOR_DECLARATION),
        };

        ourTags.put("parameter", PARAMETER);
        ourTags.put("function", FUNCTION);
        ourTags.put("local.variable", LOCAL_VARIABLE);
        ourTags.put("label", LABEL);
        ourTags.put("class", CLASS);
        ourTags.put("keyword", KEYWORD);
        ourTags.put("metadata", METADATA);
        ourTags.put("builtin", BUILTIN);
        ourTags.put("instance.member.function", INSTANCE_MEMBER_FUNCTION);
        ourTags.put("instance.member.function.call", INSTANCE_MEMBER_FUNCTION_CALL);
        ourTags.put("static.member.function", STATIC_MEMBER_FUNCTION);
        ourTags.put("instance.member.variable", INSTANCE_MEMBER_VARIABLE);
        ourTags.put("static.member.variable", STATIC_MEMBER_VARIABLE);
        ourTags.put("escape", VALID_STRING_ESCAPE);
        ourTags.put("bad.escape", INVALID_STRING_ESCAPE);
        ourTags.put("constructor.call", CONSTRUCTOR_CALL);
        ourTags.put("constructor.decl", CONSTRUCTOR_DECLARATION);
        ourTags.put("abstract.call", ABSTRACT_MEMBER_FUNCTION_CALL);
        ourTags.put("inherited.call", INHERITED_MEMBER_FUNCTION_CALL);
        ourTags.put("top.level.var.call", TOP_LEVEL_VARIABLE_ACCESS);
        ourTags.put("top.level.var.decl", TOP_LEVEL_VARIABLE_DECLARATION);
        ourTags.put("top.level.func.call", TOP_LEVEL_FUNCTION_CALL);
        ourTags.put("top.level.func.decl", TOP_LEVEL_FUNCTION_DECLARATION);
    }

    @Override
    @Nonnull
    public LocalizeValue getDisplayName() {
        return DartLocalize.dartTitle();
    }

    @Override
    @Nonnull
    public AttributesDescriptor[] getAttributeDescriptors() {
        return ATTRS;
    }

    @Override
    @Nonnull
    public SyntaxHighlighter getHighlighter() {
        return new DartSyntaxHighlighter();
    }

    @Override
    @Nonnull
    public String getDemoText() {
        return "/**\n" +
            " * documentation\n" +
            " */\n" +
            "<metadata>@Metadata</metadata>('text')\n" +
            "class <class>SomeClass</class> extends BaseClass <keyword>implements</keyword> <class>OtherClass</class> {\n" +
            "  /// documentation\n" +
            "  var <instance.member.variable>someField</instance.member.variable> = null; // line comment\n" +
            "  var <instance.member.variable>someString</instance.member.variable> = \"Escape sequences: <escape>\\n</escape> " +
            "<escape>\\xFF</escape> <escape>\\u1234</escape> <escape>\\u{2F}</escape>\"\n" +
            "  <class>String</class> <instance.member.variable>otherString</instance.member.variable> = \"Invalid escape sequences: <bad" +
            ".escape>\\xZZ</bad.escape> <bad.escape>\\uXYZZ</bad.escape> <bad.escape>\\u{XYZ}</bad.escape>\"\n" +
            "  <keyword>static</keyword> <builtin>num</builtin> <static.member.variable>staticField</static.member.variable> = 12345.67890;\n" +
            "\n" +
            "  <keyword>static</keyword> <static.member.function>staticFunction</static.member.function>() {\n" +
            "    <label>label</label>: <static.member.variable>staticField</static.member.variable>++; /* block comment */\n" +
            "  }\n\n" +
            "  <constructor.decl>SomeClass</constructor.decl>(this.someString);\n" +
            "\n" +
            "  <instance.member.function>foo</instance.member.function>(<builtin>dynamic</builtin> <parameter>param</parameter>) {\n" +
            "    <top.level.func.call>print</top.level.func.call>(<instance.member.variable>someString</instance.member.variable> + " +
            "<parameter>param</parameter>);\n" +
            "    var <local.variable>localVar</local.variable> = <class>SomeClass</class>.<static.member.variable>staticField</static.member" +
            ".variable>; \n" +
            "    var <local.variable>localVar2</local.variable> = new <constructor.call>SomeClass</constructor.call>('content').<instance.member" +
            ".function.call>bar</instance.member.function.call>();\n" +
            "    <local.variable>localVar</local.variable>++; \n" +
            "    <function>localFunction</function>() {\n" +
            "      <local.variable>localVar</local.variable> = ```; // bad character\n" +
            "    };\n" +
            "  }\n" +
            "  <builtin>int</builtin> <instance.member.function>f</instance.member.function>() => 13;\n" +
            "}\n\n" +
            "<keyword>abstract</keyword> class BaseClass {\n" +
            "  <builtin>int</builtin> g() => <abstract.call>f</abstract.call>();\n" +
            "  <builtin>int</builtin> f();\n" +
            "}\n\n" +
            "var <top.level.var.decl>topLevelVar</top.level.var.decl> = new SomeClass(null).<inherited.call>g</inherited.call>();\n\n" +
            "<top.level.func.decl>main</top.level.func.decl>() {\n" +
            "  <top.level.func.call>print</top.level.func.call>(<top.level.var.call>topLevelVar</top.level.var.call>);\n" +
            "}\n";
    }

    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return ourTags;
    }
}
