package com.jetbrains.lang.dart.ide.copyright;

import javax.annotation.Nonnull;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.jetbrains.lang.dart.psi.DartFile;
import com.maddyhome.idea.copyright.CopyrightProfile;
import com.maddyhome.idea.copyright.psi.UpdateCopyrightsProvider;
import com.maddyhome.idea.copyright.psi.UpdatePsiFileCopyright;
import com.maddyhome.idea.copyright.ui.TemplateCommentPanel;
import consulo.copyright.config.CopyrightFileConfig;

/**
 * @author Fedor.Korotkov
 */
public class UpdateDartCopyrightsProvider extends UpdateCopyrightsProvider<CopyrightFileConfig>
{
	@Nonnull
	@Override
	public UpdatePsiFileCopyright<CopyrightFileConfig> createInstance(@Nonnull PsiFile file, @Nonnull CopyrightProfile copyrightProfile)
	{
		return new UpdatePsiFileCopyright<CopyrightFileConfig>(file, copyrightProfile)
		{
			@Override
			protected boolean accept()
			{
				return getFile() instanceof DartFile;
			}

			@Override
			protected void scanFile()
			{
				PsiElement first = getFile().getFirstChild();
				PsiElement last = first;
				PsiElement next = first;
				while(next != null)
				{
					if(next instanceof PsiComment || next instanceof PsiWhiteSpace)
					{
						next = getNextSibling(next);
					}
					else
					{
						break;
					}
					last = next;
				}

				if(first != null)
				{
					checkComments(first, last, true);
				}
				else
				{
					checkComments(null, null, true);
				}
			}
		};
	}

	@Nonnull
	@Override
	public CopyrightFileConfig createDefaultOptions()
	{
		return new CopyrightFileConfig();
	}

	@Nonnull
	@Override
	public TemplateCommentPanel createConfigurable(@Nonnull Project project, @Nonnull TemplateCommentPanel parentPane, @Nonnull FileType fileType)
	{
		return new TemplateCommentPanel(fileType, parentPane, project);
	}
}

