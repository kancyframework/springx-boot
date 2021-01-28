/*
 * Created by JFormDesigner on Sat Jan 09 17:05:07 CST 2021
 */

package com.github.kancyframework.springx.swing.form;

import javax.swing.*;

/**
 * @author kancy
 */
public class TextFieldForm<T extends JTextField>  extends TextForm<JTextField> implements Value<String> {

    public TextFieldForm(String labelName) {
        super(labelName);
    }

    public TextFieldForm(String labelName, int labelWidth) {
        super(labelName, labelWidth);
    }

    public TextFieldForm(String labelName, int labelWidth, int inputWidth) {
        super(labelName, labelWidth, inputWidth);
    }

}
