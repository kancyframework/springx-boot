package com.github.kancyframework.springx.swing.dialog;

import java.awt.*;

/**
 * MessageDialog
 *
 * @author kancy
 * @date 2020/2/16 15:51
 */
public class MessageDialog extends OptionDialog {

    private String message;

    public MessageDialog(String message) {
        this.message = message;
    }

    public MessageDialog(Component parentComponent, String message) {
        super(parentComponent);
        this.message = message;
    }

    @Override
    protected Object customizeDialogComponentView() {
        return this.message;
    }
}
