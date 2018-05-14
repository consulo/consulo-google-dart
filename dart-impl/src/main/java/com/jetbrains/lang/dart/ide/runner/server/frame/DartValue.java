package com.jetbrains.lang.dart.ide.runner.server.frame;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;
import javax.swing.Icon;

import javax.annotation.Nullable;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.xdebugger.frame.XCompositeNode;
import com.intellij.xdebugger.frame.XNamedValue;
import com.intellij.xdebugger.frame.XValueChildrenList;
import com.intellij.xdebugger.frame.XValueNode;
import com.intellij.xdebugger.frame.XValuePlace;
import com.intellij.xdebugger.frame.presentation.XNumericValuePresentation;
import com.intellij.xdebugger.frame.presentation.XRegularValuePresentation;
import com.intellij.xdebugger.frame.presentation.XStringValuePresentation;
import com.intellij.xdebugger.frame.presentation.XValuePresentation;
import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineDebugProcess;
import com.jetbrains.lang.dart.ide.runner.server.google.DebuggerUtils;
import com.jetbrains.lang.dart.ide.runner.server.google.VmCallback;
import com.jetbrains.lang.dart.ide.runner.server.google.VmObject;
import com.jetbrains.lang.dart.ide.runner.server.google.VmResult;
import com.jetbrains.lang.dart.ide.runner.server.google.VmValue;
import com.jetbrains.lang.dart.ide.runner.server.google.VmVariable;

// todo navigate to source, type
public class DartValue extends XNamedValue
{
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

	public DartValue(final @Nonnull DartCommandLineDebugProcess debugProcess, final @Nonnull VmVariable vmVariable)
	{
		super(StringUtil.notNullize(DebuggerUtils.demangleVmName(vmVariable.getName()), "<unknown>"));
		myDebugProcess = debugProcess;
		myVmVariable = vmVariable;
	}

	public DartValue(@Nonnull final DartCommandLineDebugProcess debugProcess, @Nonnull @SuppressWarnings("NullableProblems") final VmValue vmValue)
	{
		super("result");
		myDebugProcess = debugProcess;
		myVmVariable = null;
		myVmValue = vmValue;
	}

	@Override
	public void computePresentation(final @Nonnull XValueNode node, final @Nonnull XValuePlace place)
	{
		ApplicationManager.getApplication().executeOnPooledThread(new Runnable()
		{
			public void run()
			{
				if(node.isObsolete())
				{
					return;
				}

				if(myVmValue == null && myVmVariable != null)
				{
					myVmValue = myVmVariable.getValue();
				}

				if(myVmValue == null)
				{
					node.setPresentation(AllIcons.Debugger.Value, null, "<no value>", false);
					return;
				}

				final String value = StringUtil.notNullize(myVmValue.getText(), "null");
				final XValuePresentation presentation;

				if(myVmValue.isNull())
				{
					presentation = new XRegularValuePresentation("null", null);
				}
				else if(myVmValue.isString())
				{
					presentation = new XStringValuePresentation(StringUtil.stripQuotesAroundValue(value));
				}
				else if(myVmValue.isNumber())
				{
					presentation = new XNumericValuePresentation(value);
				}
				else
				{
					final int objectId = myVmValue.getObjectId();
					final String postfix = objectId == 0 ? "" : "[id=" + objectId + "]";

					if(value.startsWith(OBJECT_OF_TYPE_PREFIX))
					{
						presentation = new XRegularValuePresentation("", value.substring(OBJECT_OF_TYPE_PREFIX.length()) + postfix);
					}
					else
					{
						presentation = new XRegularValuePresentation(value, DebuggerUtils.demangleVmName(myVmValue.getKind()) + postfix);
					}
				}

				final boolean neverHasChildren = myVmValue.isPrimitive() || myVmValue.isNull() || myVmValue.isFunction();
				node.setPresentation(getIcon(myVmValue), presentation, !neverHasChildren);
			}
		});
	}

	private static Icon getIcon(final @Nonnull VmValue vmValue)
	{
		if(vmValue.isList())
		{
			return AllIcons.Debugger.Db_array;
		}
		if(vmValue.isPrimitive() || vmValue.isNull())
		{
			return AllIcons.Debugger.Db_primitive;
		}
		if(vmValue.isFunction())
		{
			return AllIcons.Nodes.Function;
		}

		return AllIcons.Debugger.Value; // todo m.b. resolve and show corresponding icon?
	}

	@Override
	public void computeChildren(final @Nonnull XCompositeNode node)
	{
		// myVmValue is already calculated in computePresentation()
		if(myVmValue == null)
		{
			node.addChildren(XValueChildrenList.EMPTY, true);
		}

		// see com.google.dart.tools.debug.core.server.ServerDebugValue#fillInFieldsSync()
		try
		{
			myDebugProcess.getVmConnection().getObjectProperties(myVmValue.getIsolate(), myVmValue.getObjectId(), new VmCallback<VmObject>()
			{
				@Override
				public void handleResult(final VmResult<VmObject> result)
				{
					if(node.isObsolete())
					{
						return;
					}

					final VmObject vmObject = result == null ? null : result.getResult();
					final List<VmVariable> fields = vmObject == null ? null : vmObject.getFields();

					if(fields == null || result.isError())
					{
						return;
					}

					// todo sort somehow?
					final XValueChildrenList childrenList = new XValueChildrenList(fields.size());
					for(final VmVariable field : fields)
					{
						childrenList.add(new DartValue(myDebugProcess, field));
					}

					node.addChildren(childrenList, true);
				}
			});
		}
		catch(IOException e)
		{
			DartCommandLineDebugProcess.LOG.error(e);
		}
	}
}