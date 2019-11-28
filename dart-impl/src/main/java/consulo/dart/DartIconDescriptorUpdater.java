package consulo.dart;

import com.intellij.psi.PsiElement;
import com.jetbrains.lang.dart.DartComponentType;
import consulo.annotation.access.RequiredReadAction;
import consulo.ide.IconDescriptor;
import consulo.ide.IconDescriptorUpdater;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 12.09.13.
 */
public class DartIconDescriptorUpdater implements IconDescriptorUpdater
{
	@RequiredReadAction
	@Override
	public void updateIcon(@Nonnull IconDescriptor iconDescriptor, @Nonnull PsiElement element, int flags)
	{
		DartComponentType dartComponentType = DartComponentType.typeOf(element);
		if(dartComponentType != null)
		{
			iconDescriptor.setMainIcon(dartComponentType.getIcon());
		}
	}
}
