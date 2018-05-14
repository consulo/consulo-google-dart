package com.jetbrains.lang.dart.resolve;

import java.util.Set;

import javax.annotation.Nonnull;

import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponentName;

public class ClassNameScopeProcessor extends DartPsiScopeProcessor
{

	private final
	@Nonnull
	Set<DartComponentName> myResult;

	public ClassNameScopeProcessor(final @Nonnull Set<DartComponentName> result)
	{
		this.myResult = result;
	}

	@Override
	protected boolean doExecute(final @Nonnull DartComponentName componentName)
	{
		if(componentName.getParent() instanceof DartClass)
		{
			myResult.add(componentName);
		}
		return true;
	}
}
