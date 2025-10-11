package com.jetbrains.lang.dart.ide.generation;

import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponent;
import consulo.google.dart.localize.DartLocalize;
import consulo.localize.LocalizeValue;
import consulo.util.collection.ContainerUtil;
import consulo.util.lang.Pair;
import consulo.util.lang.function.Condition;

import java.util.List;
import java.util.Map;

/**
 * Created by fedorkorotkov.
 */
public class DartGenerateConstructorHandler extends BaseDartGenerateHandler {
  @Override
  protected LocalizeValue getTitle() {
    return DartLocalize.dartGenerateConstructor();
  }

  @Override
  protected BaseCreateMethodsFix createFix(DartClass dartClass) {
    return new CreateConstructorFix(dartClass);
  }

  @Override
  protected void collectCandidates(Map<Pair<String, Boolean>, DartComponent> classMembersMap,
                                   Map<Pair<String, Boolean>, DartComponent> superClassesMembersMap,
                                   Map<Pair<String, Boolean>, DartComponent> superInterfacesMembersMap,
                                   List<DartComponent> candidates) {
    candidates.addAll(ContainerUtil.findAll(classMembersMap.values(), new Condition<DartComponent>() {
      @Override
      public boolean value(DartComponent component) {
        return DartComponentType.typeOf(component) == DartComponentType.FIELD;
      }
    }));
  }
}
