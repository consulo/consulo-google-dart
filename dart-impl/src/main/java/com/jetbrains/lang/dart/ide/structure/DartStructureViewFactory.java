package com.jetbrains.lang.dart.ide.structure;

import javax.annotation.Nonnull;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder;
import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;

/**
 * @author Fedor.Korotkov
 */
public class DartStructureViewFactory implements PsiStructureViewFactory
{
	@Override
	public StructureViewBuilder getStructureViewBuilder(final PsiFile psiFile)
	{
		return new TreeBasedStructureViewBuilder()
		{
			@Override
			@Nonnull
			public StructureViewModel createStructureViewModel(Editor editor)
			{
				return new DartStructureViewModel(psiFile);
			}

			@Override
			public boolean isRootNodeShown()
			{
				return false;
			}
		};
	}
}
