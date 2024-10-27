package consulo.dart.template;

import com.jetbrains.lang.dart.ide.template.DartTemplateContextType;
import consulo.annotation.component.ExtensionImpl;
import consulo.google.dart.localize.DartLocalize;
import consulo.language.editor.template.LiveTemplateContributor;
import consulo.localize.LocalizeValue;
import jakarta.annotation.Nonnull;

@ExtensionImpl
public class DartLiveTemplateContributor implements LiveTemplateContributor {
  @Override
  @Nonnull
  public String groupId() {
    return "dart";
  }

  @Override
  @Nonnull
  public LocalizeValue groupName() {
    return LocalizeValue.localizeTODO("Dart");
  }

  @Override
  public void contribute(@Nonnull LiveTemplateContributor.Factory factory) {
    try(Builder builder = factory.newBuilder("dartIter", "iter", "for ($VAR$ in $ITERABLE$) {\n"
        + "  $END$\n"
        + "}", DartLocalize.livetemplateDescriptionIter())) {
      builder.withReformat();

      builder.withVariable("ITERABLE", "dartIterableVariable()", "\"array \"", true);
      builder.withVariable("VAR", "dartSuggestVariableName()", "\"o\"", true);

      builder.withContext(DartTemplateContextType.class, true);
    }

    try(Builder builder = factory.newBuilder("dartItar", "itar", "for (var $INDEX$ = 0; i < $ARRAY$.length; ++$INDEX$) {\n"
        + "  var $VAR$ = $ARRAY$[$INDEX$];\n"
        + "  $END$\n"
        + "}", DartLocalize.livetemplateDescriptionItar())) {
      builder.withReformat();

      builder.withVariable("INDEX", "dartSuggestIndexName()", "\"i\"", true);
      builder.withVariable("ARRAY", "dartListVariable()", "\"array\"", true);
      builder.withVariable("VAR", "dartSuggestVariableName()", "\"o\"", true);

      builder.withContext(DartTemplateContextType.class, true);
    }

    try (Builder builder = factory.newBuilder("dartSv", "sv", "static var $END$", LocalizeValue.localizeTODO("static var"))) {
      builder.withContext(DartTemplateContextType.class, true);
    }

    try (Builder builder = factory.newBuilder("dartFs",
                                              "fs",
                                              "$RETURN_TYPE$ $NAME$($ARGS$) => $END$ ;",
                                              LocalizeValue.localizeTODO("simple function"))) {
      builder.withReformat();

      builder.withVariable("NAME", "", "\"name\"", true);
      builder.withVariable("ARGS", "", "", true);
      builder.withVariable("RETURN_TYPE", "", "", true);

      builder.withContext(DartTemplateContextType.class, true);
    }

    try (Builder builder = factory.newBuilder("dartFb", "fb", "$RETURN_TYPE$ $NAME$($ARGS$) {\n"
      + " $END$ \n"
      + "}", LocalizeValue.localizeTODO("function with body"))) {
      builder.withReformat();

      builder.withVariable("NAME", "", "\"name\"", true);
      builder.withVariable("ARGS", "", "", true);
      builder.withVariable("RETURN_TYPE", "", "", true);

      builder.withContext(DartTemplateContextType.class, true);
    }

    try (Builder builder = factory.newBuilder("dartSfs",
                                              "sfs",
                                              "static $RETURN_TYPE$ $NAME$($ARGS$) => $END$ ;",
                                              LocalizeValue.localizeTODO("static simple function"))) {
      builder.withReformat();

      builder.withVariable("NAME", "", "\"name\"", true);
      builder.withVariable("ARGS", "", "", true);
      builder.withVariable("RETURN_TYPE", "", "", true);

      builder.withContext(DartTemplateContextType.class, true);
    }

    try (Builder builder = factory.newBuilder("dartSfb", "sfb", "static $RETURN_TYPE$ $NAME$($ARGS$) {\n"
      + " $END$ \n"
      + "}", LocalizeValue.localizeTODO("static function with body"))) {
      builder.withReformat();

      builder.withVariable("NAME", "", "\"name\"", true);
      builder.withVariable("ARGS", "", "", true);
      builder.withVariable("RETURN_TYPE", "", "", true);

      builder.withContext(DartTemplateContextType.class, true);
    }

    try (Builder builder = factory.newBuilder("surroundI", "I", "for ($VAR$ in $SELECTION$) {\n"
      + "  $END$\n"
      + "}", DartLocalize.livetemplateDescriptionIter())) {
      builder.withReformat();

      builder.withVariable("SELECTION", "", "", false);
      builder.withVariable("VAR", "dartSuggestVariableName()", "\"o\"", true);

      builder.withContext(DartTemplateContextType.class, true);
    }
  }
}
