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

    private int initValue = 0;
    private int minimum = 0;
    private int maximum = Integer.MAX_VALUE;
    private int stepSize = 1;

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
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(initValue, minimum, maximum, stepSize));
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

    public void setInitValue(int initValue) {
        this.initValue = initValue;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }
}
