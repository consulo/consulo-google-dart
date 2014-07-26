package com.jetbrains.lang.dart.sdk.listPackageDirs;

import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.Set;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.table.JBTable;

public class DartListPackageDirsDialog extends DialogWrapper
{

	public static final int CONFIGURE_NONE_EXIT_CODE = NEXT_USER_EXIT_CODE;

	private JPanel myMainPanel;
	private JBTable myTable;

	private
	@NotNull
	final Map<String, Set<String>> myPackageMap;

	protected DartListPackageDirsDialog(final @NotNull Project project, final @NotNull Map<String, Set<String>> packageMap)
	{
		super(project);
		myPackageMap = packageMap;
		setTitle("Dart Package List");
		initTable();
		setOKButtonText("Configure all");

		init();
	}

	private void initTable()
	{
		final String[][] data = new String[myPackageMap.size()][2];

		int i = 0;
		for(Map.Entry<String, Set<String>> entry : myPackageMap.entrySet())
		{
			data[i][0] = entry.getKey();
			data[i][1] = FileUtil.toSystemDependentName(StringUtil.join(entry.getValue(), "; "));
			i++;
		}

		myTable.setModel(new DefaultTableModel(data, new String[]{
				"Package name",
				"Location"
		})
		{
			@Override
			public boolean isCellEditable(final int row, final int column)
			{
				return false;
			}
		});

		final int width = new JLabel("Package name").getPreferredSize().width * 4 / 3;
		myTable.getColumnModel().getColumn(0).setPreferredWidth(width);
		myTable.getColumnModel().getColumn(1).setPreferredWidth(500 - width);
	}

	@Override
	@Nullable
	protected JComponent createCenterPanel()
	{
		return myMainPanel;
	}

	@Override
	@NotNull
	protected Action[] createActions()
	{
		if(SystemInfo.isMac)
		{
			return new Action[]{
					getCancelAction(),
					new ConfigureNoneAction(),
					getOKAction()
			};
		}
		return new Action[]{
				getOKAction(),
				new ConfigureNoneAction(),
				getCancelAction()
		};
	}

	@Override
	@Nullable
	protected String getDimensionServiceKey()
	{
		return "DartPackageListDialogDimensions";
	}

	private class ConfigureNoneAction extends DialogWrapperAction
	{
		protected ConfigureNoneAction()
		{
			super("Configure none");
		}

		@Override
		protected void doAction(final ActionEvent e)
		{
			close(CONFIGURE_NONE_EXIT_CODE);
		}
	}
}
