package com.github.kancyframework.springx.swing.dialog;

import javax.swing.*;
import java.awt.*;

/**
 * JSpinnerInputDialog
 *
 * @author huangchengkang
 * @date 2021/8/28 18:26
 */
public class JSpinnerInputDialog extends InputDialog {

    private int width = 150;
    private int height = 30;

    public JSpinnerInputDialog() {
    }

    public JSpinnerInputDialog(String inputPrompt) {
        super(inputPrompt);
    }

    public JSpinnerInputDialog(Component parentComponent, String inputPrompt) {
        super(parentComponent, inputPrompt);
    }

    @Override
    protected JComponent getInputComponent() {
        int min = 0;
        int max = 100000;
        int step = 1;  //步数间隔
        int initValue = 0;  //初始值
        SpinnerModel model = new SpinnerNumberModel(initValue, min, max, step);
        JSpinner spinner = new JSpinner(model);
        spinner.setPreferredSize(new Dimension(width, height));
        return spinner;
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
