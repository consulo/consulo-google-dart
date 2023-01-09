package com.jetbrains.lang.dart.ide.actions.ui;

import consulo.application.ApplicationPropertiesComponent;
import consulo.fileChooser.FileChooserDescriptor;
import consulo.fileChooser.IdeaFileChooser;
import consulo.project.Project;
import consulo.ui.ex.awt.DialogWrapper;
import consulo.ui.ex.awt.TextFieldWithBrowseButton;
import consulo.util.io.FileUtil;
import consulo.virtualFileSystem.VirtualFile;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author: Fedor.Korotkov
 */
public class Dart2JSSettingsDialog extends DialogWrapper {
  private JLabel myOutputLabel;
  private TextFieldWithBrowseButton myOutputFilePath;
  private JCheckBox myCheckedMode;
  private JCheckBox myMinify;
  private JPanel myMainPanel;

  public Dart2JSSettingsDialog(@Nullable Project project, String jsFilePath) {
    super(project, true);
    myOutputFilePath.setText(FileUtil.toSystemDependentName(jsFilePath));

    myCheckedMode.setSelected(ApplicationPropertiesComponent.getInstance().getBoolean("dart2js.checked.mode", false));
    myMinify.setSelected(ApplicationPropertiesComponent.getInstance().getBoolean("dart2js.minify", false));

    myCheckedMode.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ApplicationPropertiesComponent.getInstance()
                                      .setValue("dart2js.checked.mode", Boolean.toString(myCheckedMode.isSelected()));
      }
    });
    myMinify.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ApplicationPropertiesComponent.getInstance().setValue("dart2js.minify", Boolean.toString(myMinify.isSelected()));
      }
    });
    myOutputFilePath.getButton().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        final FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
        final VirtualFile file = IdeaFileChooser.chooseFile(descriptor, myMainPanel, null, null);
        if (file != null) {
          myOutputFilePath.setText(FileUtil.toSystemDependentName(file.getPath()));
        }
      }
    });

    setTitle("Dart2JS");
    init();
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    return myMainPanel;
  }

  public String getOutputPath() {
    return FileUtil.toSystemIndependentName(myOutputFilePath.getText());
  }

  public boolean isCheckedMode() {
    return myCheckedMode.isSelected();
  }

  public boolean isMinify() {
    return myMinify.isSelected();
  }
}
