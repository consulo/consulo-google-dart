package com.jetbrains.lang.dart.sdk.listPackageDirs;

import consulo.content.library.LibraryProperties;
import consulo.util.xml.serializer.XmlSerializerUtil;
import consulo.util.xml.serializer.annotation.MapAnnotation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class DartListPackageDirsLibraryProperties extends LibraryProperties<DartListPackageDirsLibraryProperties> {

  private
  @Nonnull
  Map<String, Set<String>> myPackageNameToDirsMap;

  public DartListPackageDirsLibraryProperties() {
    myPackageNameToDirsMap = new TreeMap<String, Set<String>>();
  }

  @Nonnull
  @MapAnnotation(surroundWithTag = false, surroundKeyWithTag = false)
  public Map<String, Set<String>> getPackageNameToDirsMap() {
    return myPackageNameToDirsMap;
  }

  public void setPackageNameToDirsMap(@Nonnull final Map<String, Set<String>> packageNameToDirsMap) {
    myPackageNameToDirsMap = packageNameToDirsMap;
  }

  @Override
  @Nullable
  public DartListPackageDirsLibraryProperties getState() {
    return this;
  }

  @Override
  public void loadState(final DartListPackageDirsLibraryProperties state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  @Override
  public boolean equals(final Object obj) {
    return obj instanceof DartListPackageDirsLibraryProperties && myPackageNameToDirsMap.equals(((DartListPackageDirsLibraryProperties)obj)
                                                                                                  .getPackageNameToDirsMap());
  }

  @Override
  public int hashCode() {
    return myPackageNameToDirsMap.hashCode();
  }
}
