package com.github.kancyframework.springx.swing.dialog;

import javax.swing.*;
import java.awt.*;

/**
 * InputDialog
 *
 * @author kancy
 * @date 2020/2/16 11:28
 */
public class JTextAreaInputDialog extends InputDialog {

    private int width = 450;
    private int height = 150;

    public JTextAreaInputDialog() {
    }

    public JTextAreaInputDialog(String inputPrompt) {
        super(inputPrompt);
    }

    public JTextAreaInputDialog(Component parentComponent, String inputPrompt) {
        super(parentComponent, inputPrompt);
    }

    @Override
    protected JComponent getInputComponent() {
        JTextArea input = new JTextArea();
        input.setLineWrap(true);
        input.setForeground(Color.BLUE);
        JScrollPane jScrollPane = new JScrollPane(input);
        jScrollPane.setPreferredSize(new Dimension(width, height));
        return jScrollPane;
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
