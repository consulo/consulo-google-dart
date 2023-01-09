package com.jetbrains.lang.dart.util;

import consulo.document.Document;
import consulo.document.FileDocumentManager;
import consulo.util.dataholder.Key;
import consulo.util.lang.Pair;
import consulo.virtualFileSystem.VirtualFile;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class PubspecYamlUtil {

  public static final String PUBSPEC_YAML = "pubspec.yaml";

  public static final String NAME = "name";
  public static final String DEPENDENCIES = "dependencies";
  public static final String DEV_DEPENDENCIES = "dev_dependencies";
  public static final String PATH = "path";

  public static final String LIB_DIRECTORY_NAME = "lib";

  private static final Key<Pair<Long, Map<String, Object>>> MOD_STAMP_TO_PUBSPEC_NAME = Key.create("MOD_STAMP_TO_PUBSPEC_NAME");

  @Nullable
  public static Map<String, Object> getPubspecYamlInfo(final @Nonnull VirtualFile pubspecYamlFile) {
    // do not use Yaml plugin here - IntelliJ IDEA Community Edition doesn't contain it.
    Pair<Long, Map<String, Object>> data = pubspecYamlFile.getUserData(MOD_STAMP_TO_PUBSPEC_NAME);

    final FileDocumentManager documentManager = FileDocumentManager.getInstance();
    final Document cachedDocument = documentManager.getCachedDocument(pubspecYamlFile);
    final Long currentTimestamp = cachedDocument != null ? cachedDocument.getModificationStamp() : pubspecYamlFile.getModificationCount();
    final Long cachedTimestamp = data == null ? null : data.first;

    if (cachedTimestamp == null || !cachedTimestamp.equals(currentTimestamp)) {
      data = null;
      pubspecYamlFile.putUserData(MOD_STAMP_TO_PUBSPEC_NAME, null);
      final Map<String, Object> pubspecYamlInfo;
      if (cachedDocument != null) {
        pubspecYamlInfo = loadPubspecYamlInfo(cachedDocument.getText());
      }
      else {
        pubspecYamlInfo = loadPubspecYamlInfo(pubspecYamlFile.loadText().toString());
      }

      if (pubspecYamlInfo != null) {
        data = Pair.create(currentTimestamp, pubspecYamlInfo);
        pubspecYamlFile.putUserData(MOD_STAMP_TO_PUBSPEC_NAME, data);
      }
    }

    return data == null ? null : data.second;
  }

  @Nullable
  private static Map<String, Object> loadPubspecYamlInfo(final @Nonnull String pubspecYamlFileContents) {
    // see com.google.dart.tools.core.utilities.yaml.PubYamlUtils#parsePubspecYamlToMap()
    // deprecated constructor used to be compatible with old snakeyaml version in testng.jar (it wins when running from sources or tests)
    //noinspection deprecation
    final Yaml yaml = new Yaml(new SafeConstructor(), new Representer(), new DumperOptions(), new Resolver() {
      @Override
      protected void addImplicitResolvers() {
        addImplicitResolver(Tag.BOOL, BOOL, "yYnNtTfFoO");
        addImplicitResolver(Tag.NULL, NULL, "~nN\0");
        addImplicitResolver(Tag.NULL, EMPTY, null);
        addImplicitResolver(new Tag(Tag.PREFIX + "value"), VALUE, "=");
        addImplicitResolver(Tag.MERGE, MERGE, "<");
      }
    });

    try {
      //noinspection unchecked
      return (Map<String, Object>)yaml.load(pubspecYamlFileContents);
    }
    catch (Exception e) {
      return null; // malformed yaml, e.g. because of typing in it
    }
  }
}
