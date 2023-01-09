package com.jetbrains.lang.dart.ide.runner.unittest;

import consulo.execution.action.Location;
import consulo.execution.test.TestLocationProvider;
import consulo.project.Project;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author: Fedor.Korotkov
 */
public class DartTestLocationProvider implements TestLocationProvider {
  @Nonnull
  @Override
  public List<Location> getLocation(@Nonnull String protocolId, @Nonnull String locationData, Project project) {
    return null;
  }
}
