package com.jetbrains.lang.dart.sdk.listPackageDirs;

import consulo.application.util.SystemInfo;
import consulo.project.Project;
import consulo.ui.ex.awt.DialogWrapper;
import consulo.ui.ex.awt.table.JBTable;
import consulo.util.io.FileUtil;
import consulo.util.lang.StringUtil;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.Set;

public class DartListPackageDirsDialog extends DialogWrapper {

  public static final int CONFIGURE_NONE_EXIT_CODE = NEXT_USER_EXIT_CODE;

  private JPanel myMainPanel;
  private JBTable myTable;

  private
  @Nonnull
  final Map<String, Set<String>> myPackageMap;

  protected DartListPackageDirsDialog(final @Nonnull Project project, final @Nonnull Map<String, Set<String>> packageMap) {
    super(project);
    myPackageMap = packageMap;
    setTitle("Dart Package List");
    initTable();
    setOKButtonText("Configure all");

    init();
  }

  private void initTable() {
    final String[][] data = new String[myPackageMap.size()][2];

    int i = 0;
    for (Map.Entry<String, Set<String>> entry : myPackageMap.entrySet()) {
      data[i][0] = entry.getKey();
      data[i][1] = FileUtil.toSystemDependentName(StringUtil.join(entry.getValue(), "; "));
      i++;
    }

    myTable.setModel(new DefaultTableModel(data, new String[]{
      "Package name",
      "Location"
    }) {
      @Override
      public boolean isCellEditable(final int row, final int column) {
        return false;
      }
    });

    final int width = new JLabel("Package name").getPreferredSize().width * 4 / 3;
    myTable.getColumnModel().getColumn(0).setPreferredWidth(width);
    myTable.getColumnModel().getColumn(1).setPreferredWidth(500 - width);
  }

  @Override
  @Nullable
  protected JComponent createCenterPanel() {
    return myMainPanel;
  }

  @Override
  @Nonnull
  protected Action[] createActions() {
    if (SystemInfo.isMac) {
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
  protected String getDimensionServiceKey() {
    return "DartPackageListDialogDimensions";
  }

  private class ConfigureNoneAction extends DialogWrapperAction {
    protected ConfigureNoneAction() {
      super("Configure none");
    }

    @Override
    protected void doAction(final ActionEvent e) {
      close(CONFIGURE_NONE_EXIT_CODE);
    }
  }
}
