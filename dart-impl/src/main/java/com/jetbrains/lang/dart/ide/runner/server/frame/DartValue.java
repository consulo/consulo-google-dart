package com.jetbrains.lang.dart.ide.runner.server.frame;

import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineDebugProcess;
import com.jetbrains.lang.dart.ide.runner.server.google.*;
import consulo.application.ApplicationManager;
import consulo.execution.debug.frame.*;
import consulo.execution.debug.frame.presentation.XNumericValuePresentation;
import consulo.execution.debug.frame.presentation.XRegularValuePresentation;
import consulo.execution.debug.frame.presentation.XStringValuePresentation;
import consulo.execution.debug.frame.presentation.XValuePresentation;
import consulo.execution.debug.icon.ExecutionDebugIconGroup;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.ui.image.Image;
import consulo.util.lang.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

// todo navigate to source, type
public class DartValue extends XNamedValue {
  private final
  @Nonnull
  DartCommandLineDebugProcess myDebugProcess;
  private final
  @Nullable
  VmVariable myVmVariable;
  private
  @Nullable
  VmValue myVmValue;

  private static final String OBJECT_OF_TYPE_PREFIX = "object of type ";

  public DartValue(final @Nonnull DartCommandLineDebugProcess debugProcess, final @Nonnull VmVariable vmVariable) {
    super(StringUtil.notNullize(DebuggerUtils.demangleVmName(vmVariable.getName()), "<unknown>"));
    myDebugProcess = debugProcess;
    myVmVariable = vmVariable;
  }

  public DartValue(@Nonnull final DartCommandLineDebugProcess debugProcess,
                   @Nonnull @SuppressWarnings("NullableProblems") final VmValue vmValue) {
    super("result");
    myDebugProcess = debugProcess;
    myVmVariable = null;
    myVmValue = vmValue;
  }

  @Override
  public void computePresentation(final @Nonnull XValueNode node, final @Nonnull XValuePlace place) {
    ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
      public void run() {
        if (node.isObsolete()) {
          return;
        }

        if (myVmValue == null && myVmVariable != null) {
          myVmValue = myVmVariable.getValue();
        }

        if (myVmValue == null) {
          node.setPresentation(ExecutionDebugIconGroup.nodeValue(), null, "<no value>", false);
          return;
        }

        final String value = StringUtil.notNullize(myVmValue.getText(), "null");
        final XValuePresentation presentation;

        if (myVmValue.isNull()) {
          presentation = new XRegularValuePresentation("null", null);
        }
        else if (myVmValue.isString()) {
          presentation = new XStringValuePresentation(StringUtil.stripQuotesAroundValue(value));
        }
        else if (myVmValue.isNumber()) {
          presentation = new XNumericValuePresentation(value);
        }
        else {
          final int objectId = myVmValue.getObjectId();
          final String postfix = objectId == 0 ? "" : "[id=" + objectId + "]";

          if (value.startsWith(OBJECT_OF_TYPE_PREFIX)) {
            presentation = new XRegularValuePresentation("", value.substring(OBJECT_OF_TYPE_PREFIX.length()) + postfix);
          }
          else {
            presentation = new XRegularValuePresentation(value, DebuggerUtils.demangleVmName(myVmValue.getKind()) + postfix);
          }
        }

        final boolean neverHasChildren = myVmValue.isPrimitive() || myVmValue.isNull() || myVmValue.isFunction();
        node.setPresentation(getIcon(myVmValue), presentation, !neverHasChildren);
      }
    });
  }

  private static Image getIcon(final @Nonnull VmValue vmValue) {
    if (vmValue.isList()) {
      return ExecutionDebugIconGroup.nodeArray();
    }
    if (vmValue.isPrimitive() || vmValue.isNull()) {
      return ExecutionDebugIconGroup.nodePrimitive();
    }
    if (vmValue.isFunction()) {
      return PlatformIconGroup.nodesFunction();
    }

    return ExecutionDebugIconGroup.nodeValue(); // todo m.b. resolve and show corresponding icon?
  }

  @Override
  public void computeChildren(final @Nonnull XCompositeNode node) {
    // myVmValue is already calculated in computePresentation()
    if (myVmValue == null) {
      node.addChildren(XValueChildrenList.EMPTY, true);
    }

    // see com.google.dart.tools.debug.core.server.ServerDebugValue#fillInFieldsSync()
    try {
      myDebugProcess.getVmConnection().getObjectProperties(myVmValue.getIsolate(), myVmValue.getObjectId(), new VmCallback<VmObject>() {
        @Override
        public void handleResult(final VmResult<VmObject> result) {
          if (node.isObsolete()) {
            return;
          }

          final VmObject vmObject = result == null ? null : result.getResult();
          final List<VmVariable> fields = vmObject == null ? null : vmObject.getFields();

          if (fields == null || result.isError()) {
            return;
          }

          // todo sort somehow?
          final XValueChildrenList childrenList = new XValueChildrenList(fields.size());
          for (final VmVariable field : fields) {
            childrenList.add(new DartValue(myDebugProcess, field));
          }

          node.addChildren(childrenList, true);
        }
      });
    }
    catch (IOException e) {
      DartCommandLineDebugProcess.LOG.error(e);
    }
  }
}