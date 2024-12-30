package com.jetbrains.lang.dart.psi;

import com.jetbrains.lang.dart.util.DartClassResolveResult;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.annotation.component.ServiceImpl;
import consulo.application.progress.ProgressIndicatorProvider;
import consulo.ide.ServiceManager;
import consulo.language.psi.AnyPsiChangeListener;
import consulo.project.Project;
import consulo.util.collection.ContainerUtil;
import consulo.util.collection.HashingStrategy;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Map;

/**
 * @author: Fedor.Korotkov
 */
@Singleton
@ServiceAPI(ComponentScope.PROJECT)
@ServiceImpl
public class DartClassResolveCache {
  private final Map<DartClass, DartClassResolveResult> myMap = createWeakMap();

  public static DartClassResolveCache getInstance(Project project) {
    ProgressIndicatorProvider.checkCanceled(); // We hope this method is being called often enough to cancel daemon processes smoothly
    return ServiceManager.getService(project, DartClassResolveCache.class);
  }

  @Inject
  public DartClassResolveCache(@Nonnull Project project) {
    project.getMessageBus().connect().subscribe(AnyPsiChangeListener.class, new AnyPsiChangeListener() {
      @Override
      public void beforePsiChanged(boolean isPhysical) {
        myMap.clear();
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
