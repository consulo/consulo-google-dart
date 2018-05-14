package com.jetbrains.lang.dart.psi.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.lang.dart.psi.DartType;

/**
 * @author: Fedor.Korotkov
 */
abstract public class TypedefDartPsiClass extends AbstractDartPsiClass {
	public TypedefDartPsiClass(@Nonnull ASTNode node) {
		super(node);
	}

	@Nullable
	@Override
	public DartType getSuperClass() {
		return PsiTreeUtil.getChildOfType(this, DartType.class);
	}
}
