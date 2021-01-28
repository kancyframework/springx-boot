/*
 * Created by JFormDesigner on Sat Jan 09 17:05:07 CST 2021
 */

package com.github.kancyframework.springx.swing.form;

import javax.swing.*;

/**
 * @author kancy
 */
public class TextAreaForm extends TextForm<JTextArea> implements Value<String> {

    public TextAreaForm(String labelName) {
        super(labelName, true, 50, 50, 300);
    }

    public TextAreaForm(String labelName, int labelWidth) {
        super(labelName, true, 50, labelWidth, 300);
    }

    public TextAreaForm(String labelName, int labelWidth, int inputWidth) {
        super(labelName, true, 50, labelWidth, inputWidth);
    }
}
