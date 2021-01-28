package com.github.kancyframework.springx.swing.dialog;

import javax.swing.*;
import java.awt.*;

/**
 * JPasswordFieldInputDialog
 *
 * @author kancy
 * @date 2020/2/16 14:57
 */
public class JPasswordFieldInputDialog extends InputDialog {
    private int width = 300;
    private int height = 30;

    public JPasswordFieldInputDialog(String inputPrompt) {
        super(inputPrompt);
    }

    public JPasswordFieldInputDialog(Component parentComponent, String inputPrompt) {
        super(parentComponent, inputPrompt);
    }

    @Override
    protected JComponent getInputComponent() {
        JTextField jTextField = new JPasswordField();
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
