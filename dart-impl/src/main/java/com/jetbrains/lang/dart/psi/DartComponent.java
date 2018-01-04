package com.jetbrains.lang.dart.psi;

import org.jetbrains.annotations.Nullable;
import com.intellij.psi.PsiNameIdentifierOwner;

public interface DartComponent extends DartPsiCompositeElement, PsiNameIdentifierOwner
{
	@Nullable
	DartComponentName getComponentName();

	boolean isStatic();

	boolean isPublic();

	boolean isConstructor();

	boolean isGetter();

	boolean isSetter();

	boolean isAbstract();

	DartMetadata getMetadataByName(final String name);
}
