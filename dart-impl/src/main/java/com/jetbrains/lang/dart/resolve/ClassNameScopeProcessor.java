package com.jetbrains.lang.dart.resolve;

import java.util.Set;

import org.jetbrains.annotations.NotNull;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponentName;

public class ClassNameScopeProcessor extends DartPsiScopeProcessor
{

	private final
	@NotNull
	Set<DartComponentName> myResult;

	public ClassNameScopeProcessor(final @NotNull Set<DartComponentName> result)
	{
		this.myResult = result;
	}

	@Override
	protected boolean doExecute(final @NotNull DartComponentName componentName)
	{
		if(componentName.getParent() instanceof DartClass)
		{
			myResult.add(componentName);
		}
		return true;
	}
}
