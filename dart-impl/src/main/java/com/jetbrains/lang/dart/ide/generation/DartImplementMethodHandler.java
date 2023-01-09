package com.jetbrains.lang.dart.ide.generation;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponent;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.generation.ImplementMethodHandler;
import consulo.util.lang.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtensionImpl
public class DartImplementMethodHandler extends BaseDartGenerateHandler implements ImplementMethodHandler {
  @Override
  protected String getTitle() {
    return DartBundle.message("dart.implement.method");
  }

  @Override
  protected void collectCandidates(Map<Pair<String, Boolean>, DartComponent> classMembersMap, Map<Pair<String, Boolean>,
    DartComponent> superClassesMembersMap, Map<Pair<String, Boolean>, DartComponent> superInterfacesMembersMap,
                                   List<DartComponent> candidates) {
    Map<Pair<String, Boolean>, DartComponent> result = new HashMap<Pair<String, Boolean>, DartComponent>(superInterfacesMembersMap);
    result.keySet().removeAll(superClassesMembersMap.keySet());
    for (Map.Entry<Pair<String, Boolean>, DartComponent> entry : superClassesMembersMap.entrySet()) {
      final DartComponent component = entry.getValue();
      if (component.isAbstract() && !result.containsKey(entry.getKey())) {
        result.put(entry.getKey(), entry.getValue());
      }
    }
    result.keySet().removeAll(classMembersMap.keySet());
    candidates.addAll(result.values());
  }

  @Override
  protected BaseCreateMethodsFix createFix(DartClass dartClass) {
    return new OverrideImplementMethodFix(dartClass, true);
  }
}
