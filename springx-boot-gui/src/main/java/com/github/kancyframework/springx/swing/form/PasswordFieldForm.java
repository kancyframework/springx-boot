package com.github.kancyframework.springx.swing.form;

import javax.swing.*;

/**
 * PasswordField
 *
 * @author kancy
 * @date 2021/1/9 18:03
 */
public class PasswordFieldForm extends TextFieldForm<JPasswordField> {

    public PasswordFieldForm(String labelName) {
        super(labelName);
    }

    public PasswordFieldForm(String labelName, int labelWidth) {
        super(labelName, labelWidth);
    }

    public PasswordFieldForm(String labelName, int labelWidth, int inputWidth) {
        super(labelName, labelWidth, inputWidth);
    }

}
