package com.github.kancyframework.springx.swing.dialog;

import javax.swing.*;
import java.awt.*;

/**
 * JTextFieldInputDialog
 *
 * @author kancy
 * @date 2020/2/16 14:47
 */
public class JTextFieldInputDialog extends InputDialog {

    private int width = 300;
    private int height = 30;

    public JTextFieldInputDialog() {
    }

    public JTextFieldInputDialog(String inputPrompt) {
        super(inputPrompt);
    }

    public JTextFieldInputDialog(Component parentComponent, String inputPrompt) {
        super(parentComponent, inputPrompt);
    }

    @Override
    protected JComponent getInputComponent() {
        JTextField jTextField = new JTextField();
        jTextField.setForeground(Color.BLUE);
        jTextField.setPreferredSize(new Dimension(width, height));
        return jTextField;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSize(int width, int height){
        setWidth(width);
        setHeight(height);
    }
}
