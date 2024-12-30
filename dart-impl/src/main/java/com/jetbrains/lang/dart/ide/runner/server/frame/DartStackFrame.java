package com.jetbrains.lang.dart.ide.runner.server.frame;

import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineDebugProcess;
import com.jetbrains.lang.dart.ide.runner.server.google.DebuggerUtils;
import com.jetbrains.lang.dart.ide.runner.server.google.VmCallFrame;
import com.jetbrains.lang.dart.ide.runner.server.google.VmLocation;
import com.jetbrains.lang.dart.ide.runner.server.google.VmVariable;
import consulo.execution.debug.XDebuggerUtil;
import consulo.execution.debug.XSourcePosition;
import consulo.execution.debug.evaluation.XDebuggerEvaluator;
import consulo.execution.debug.frame.XCompositeNode;
import consulo.execution.debug.frame.XStackFrame;
import consulo.execution.debug.frame.XValueChildrenList;
import consulo.ui.ex.ColoredTextContainer;
import consulo.ui.ex.SimpleTextAttributes;
import consulo.virtualFileSystem.VirtualFile;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.List;

public class DartStackFrame extends XStackFrame {
  private final
  @Nonnull
  DartCommandLineDebugProcess myDebugProcess;
  private final
  @Nonnull
  VmCallFrame myVmCallFrame;
  private final
  @Nullable
  XSourcePosition mySourcePosition;
  private final
  @Nullable
  String myLocationUrl;

  public DartStackFrame(@Nonnull final DartCommandLineDebugProcess debugProcess, final @Nonnull VmCallFrame vmCallFrame) {
    myDebugProcess = debugProcess;
    myVmCallFrame = vmCallFrame;

    final VmLocation location = vmCallFrame.getLocation();
    myLocationUrl = location == null ? null : location.getUnescapedUrl();
    if (myLocationUrl != null) {
      final VirtualFile file = myDebugProcess.getDartUrlResolver().findFileByDartUrl(myLocationUrl);
      final int line = location.getLineNumber(debugProcess.getVmConnection()) - 1;
      mySourcePosition = file == null || line < 0 ? null : XDebuggerUtil.getInstance().createPosition(file, line);
    }
    else {
      mySourcePosition = null;
    }
  }

  @Override
  @Nullable
  public Object getEqualityObject() {
    return myLocationUrl + "#" + myVmCallFrame.getFunctionName();
  }

  @Nullable
  @Override
  public XSourcePosition getSourcePosition() {
    return mySourcePosition;
  }

  @Override
  @Nullable
  public XDebuggerEvaluator getEvaluator() {
    return new DartDebuggerEvaluator(myDebugProcess, myVmCallFrame);
  }

  @Override
  public void computeChildren(final @Nonnull XCompositeNode node) {
    final List<VmVariable> locals = myVmCallFrame.getLocals();

    final XValueChildrenList childrenList = new XValueChildrenList(locals == null ? 1 : locals.size() + 1);

    if (locals != null) {
      for (final VmVariable local : locals) {
        childrenList.add(new DartValue(myDebugProcess, local));
      }
    }

    if (myVmCallFrame.getIsolate() != null && myVmCallFrame.getLibraryId() != -1) {
      // todo make library info presentable
      //childrenList.add(new DartLibraryValue(myDebugProcess, myVmCallFrame.getIsolate(), myVmCallFrame.getLibraryId()));
    }

    node.addChildren(childrenList, true);
  }

  @Override
  public void customizePresentation(@Nonnull ColoredTextContainer component) {
    final XSourcePosition position = getSourcePosition();

    if (myVmCallFrame.getFunctionName() != null) {
      component.append(DebuggerUtils.demangleVmName(myVmCallFrame.getFunctionName()), SimpleTextAttributes.REGULAR_ATTRIBUTES);
    }

    if (position != null) {
      final String text = " (" + position.getFile().getName() + ":" + (position.getLine() + 1) + ")";
      component.append(text, SimpleTextAttributes.GRAY_ATTRIBUTES);
    }
    else if (myLocationUrl != null) {
      component.append(" (" + myLocationUrl + ")", SimpleTextAttributes.GRAY_ATTRIBUTES);
    }
  }
}
