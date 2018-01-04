package com.jetbrains.lang.dart.psi.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.lang.dart.psi.DartType;

/**
 * @author: Fedor.Korotkov
 */
abstract public class TypedefDartPsiClass extends AbstractDartPsiClass {
	public TypedefDartPsiClass(@NotNull ASTNode node) {
		super(node);
	}

	@Nullable
	@Override
	public DartType getSuperClass() {
		return PsiTreeUtil.getChildOfType(this, DartType.class);
	}
}
