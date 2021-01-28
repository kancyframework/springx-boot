package com.github.kancyframework.springx.swing.form;

import javax.swing.*;
import java.awt.*;

/**
 * CheckBoxForm
 *
 * @author kancy
 * @date 2021/1/9 19:50
 */
public class CheckBoxForm extends JComponent implements Value<Boolean> {

    private JCheckBox checkBox;

    private String labelName;
    private boolean defaultValue;

    private int labelWidth = 50;
    private int inputWidth = 300;
    private int rowHeight = 0;

    public CheckBoxForm(String labelName) {
        this.labelName = labelName;
    }

    public CheckBoxForm(String labelName, boolean defaultValue) {
        this.labelName = labelName;
        this.defaultValue = defaultValue;
        initComponents();
    }

    private void initComponents() {
        checkBox = new JCheckBox();
        checkBox.setText(labelName);
        checkBox.setSelected(defaultValue);

        //======== this ========
        setLayout(new GridBagLayout());
        ((GridBagLayout)getLayout()).columnWidths = new int[] {labelWidth, inputWidth, 20};
        ((GridBagLayout)getLayout()).rowHeights = new int[] {rowHeight};

        //---- label ----
        add(new JLabel("开关"), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.CENTER,
                new Insets(0, 0, 0, 5), 0, 0));
        add(checkBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
    }

    @Override
    public Boolean getValue() {
        return checkBox.isSelected();
    }

    @Override
    public void setDefaultValue(Boolean defaultValue) {
        checkBox.setSelected(defaultValue);
    }
}
