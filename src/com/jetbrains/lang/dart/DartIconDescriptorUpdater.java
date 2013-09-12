package com.jetbrains.lang.dart;

import com.intellij.ide.IconDescriptor;
import com.intellij.ide.IconDescriptorUpdater;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 12.09.13.
 */
public class DartIconDescriptorUpdater implements IconDescriptorUpdater {
	@Override
	public void updateIcon(@NotNull IconDescriptor iconDescriptor, @NotNull PsiElement element, int flags) {
		DartComponentType dartComponentType = DartComponentType.typeOf(element);
		if(dartComponentType != null) {
			iconDescriptor.setMainIcon(dartComponentType.getIcon());
		}
	}
}
