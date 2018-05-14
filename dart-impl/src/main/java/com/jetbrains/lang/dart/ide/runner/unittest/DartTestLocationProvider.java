package com.jetbrains.lang.dart.ide.runner.unittest;

import com.intellij.execution.Location;
import com.intellij.openapi.project.Project;
import com.intellij.testIntegration.TestLocationProvider;
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
