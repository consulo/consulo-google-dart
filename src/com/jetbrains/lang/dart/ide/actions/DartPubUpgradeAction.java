package com.jetbrains.lang.dart.ide.actions;

import org.jetbrains.annotations.Nls;
import com.jetbrains.lang.dart.DartBundle;

public class DartPubUpgradeAction extends DartPubActionBase
{
	@Nls
	@Override
	protected String getPresentableText()
	{
		return DartBundle.message("dart.pub.upgrade");
	}

	@Override
	protected String getPubCommand()
	{
		return "upgrade";
	}

	@Override
	protected String getSuccessMessage()
	{
		return DartBundle.message("dart.pub.upgrade.success");
	}
}
