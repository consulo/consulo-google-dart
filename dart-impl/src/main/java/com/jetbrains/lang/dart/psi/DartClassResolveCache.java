package com.jetbrains.lang.dart.psi;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.psi.impl.AnyPsiChangeListener;
import com.intellij.psi.impl.PsiManagerImpl;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.messages.MessageBus;
import com.jetbrains.lang.dart.util.DartClassResolveResult;
import consulo.util.collection.HashingStrategy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author: Fedor.Korotkov
 */
public class DartClassResolveCache {
  private final Map<DartClass, DartClassResolveResult> myMap = createWeakMap();

  public static DartClassResolveCache getInstance(Project project) {
    ProgressIndicatorProvider.checkCanceled(); // We hope this method is being called often enough to cancel daemon processes smoothly
    return ServiceManager.getService(project, DartClassResolveCache.class);
  }

  public DartClassResolveCache(@Nonnull MessageBus messageBus) {
    messageBus.connect().subscribe(PsiManagerImpl.ANY_PSI_CHANGE_TOPIC, new AnyPsiChangeListener() {
      @Override
      public void beforePsiChanged(boolean isPhysical) {
        myMap.clear();
      }

      @Override
      public void afterPsiChanged(boolean isPhysical) {
      }
    });
  }

  private static <K, V> Map<K, V> createWeakMap() {
    return ContainerUtil.<K, V>createWeakMap(7, 0.75f, HashingStrategy.canonical());
  }

  public void put(@Nonnull DartClass dartClass, @Nonnull DartClassResolveResult result) {
    myMap.put(dartClass, result);
  }

  @Nullable
  public DartClassResolveResult get(DartClass dartClass) {
    return myMap.get(dartClass);
  }
}
