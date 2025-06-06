package com.jetbrains.lang.dart.util;

import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponent;
import com.jetbrains.lang.dart.psi.DartTypeArguments;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Fedor.Korotkov
 */
public class DartGenericSpecialization implements Cloneable {
  public static final DartGenericSpecialization EMPTY = new DartGenericSpecialization();

  final Map<String, DartClassResolveResult> map;

  public DartGenericSpecialization() {
    this(new HashMap<String, DartClassResolveResult>());
  }

  protected DartGenericSpecialization(Map<String, DartClassResolveResult> map) {
    this.map = map;
  }

  @Override
  public DartGenericSpecialization clone() {
    final Map<String, DartClassResolveResult> clonedMap = new HashMap<String, DartClassResolveResult>();
    for (String key : map.keySet()) {
      clonedMap.put(key, map.get(key));
    }
    return new DartGenericSpecialization(clonedMap);
  }

  public void put(PsiElement element, String genericName, DartClassResolveResult resolveResult) {
    map.put(getGenericKey(element, genericName), resolveResult);
  }

  public boolean containsKey(@Nullable PsiElement element, String genericName) {
    return map.containsKey(getGenericKey(element, genericName));
  }

  public DartClassResolveResult get(@Nullable PsiElement element, String genericName) {
    return map.get(getGenericKey(element, genericName));
  }

  public DartGenericSpecialization getInnerSpecialization(PsiElement element) {
    final String prefixToRemove = getGenericKey(element, "");
    final Map<String, DartClassResolveResult> result = new HashMap<String, DartClassResolveResult>();
    for (String key : map.keySet()) {
      final DartClassResolveResult value = map.get(key);
      String newKey = key;
      if (newKey.startsWith(prefixToRemove)) {
        newKey = newKey.substring(prefixToRemove.length());
      }
      result.put(newKey, value);
    }
    return new DartGenericSpecialization(result);
  }

  public static String getGenericKey(@Nullable PsiElement element, @Nonnull String genericName) {
    final StringBuilder result = new StringBuilder();
    final DartComponent dartComponent = PsiTreeUtil.getParentOfType(element, DartComponent.class, false);
    if (dartComponent instanceof DartClass) {
      result.append(dartComponent.getName());
    }
    else if (dartComponent != null) {
      DartClass dartClass = PsiTreeUtil.getParentOfType(dartComponent, DartClass.class);
      if (dartClass != null) {
        result.append(dartClass.getName());
      }
      if (PsiTreeUtil.getChildOfType(dartComponent, DartTypeArguments.class) != null) {
        // generic method
        result.append(":");
        result.append(dartComponent.getName());
      }
    }
    if (result.length() > 0) {
      result.append("-");
    }
    result.append(genericName);
    return result.toString();
  }
}
