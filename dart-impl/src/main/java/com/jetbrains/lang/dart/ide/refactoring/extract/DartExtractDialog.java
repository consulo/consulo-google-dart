package com.jetbrains.lang.dart.ide.refactoring.extract;

import com.jetbrains.lang.dart.util.DartControlFlow;
import consulo.google.dart.localize.DartLocalize;
import consulo.project.Project;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.awt.DialogWrapper;
import consulo.ui.ex.awt.ValidationInfo;
import consulo.util.lang.StringUtil;
import jakarta.annotation.Nullable;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DartExtractDialog extends DialogWrapper {
    private final DartControlFlow myScope;
    private JLabel mySignatureLabel;
    private JPanel myMainPanel;
    private JTextField myFunctionNameField;

    protected DartExtractDialog(@Nullable Project project, String functionName, DartControlFlow scope) {
        super(project);
        myScope = scope;
        myFunctionNameField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                mySignatureLabel.setText(myScope.getSignature(getFunctionName()));
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doOKAction();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        setTitle(DartLocalize.dartExtractMethod());
        init();
        mySignatureLabel.setText(myScope.getSignature(functionName));
        myFunctionNameField.setText(functionName);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return myMainPanel;
    }

    @RequiredUIAccess
    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return myFunctionNameField;
    }

    public String getFunctionName() {
        return myFunctionNameField.getText();
    }

    @RequiredUIAccess
    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if (!StringUtil.isJavaIdentifier(getFunctionName())) {
            return new ValidationInfo("Not a valid name!", myFunctionNameField);
        }
        return super.doValidate();
    }
}
