package com.github.kancyframework.springx.swing.dialog;

import java.awt.*;

/**
 * 系统属性弹框
 */
public class SystemPropertiesDialog extends PropertiesDialog {
    public SystemPropertiesDialog(Window owner) {
        super(owner, System.getProperties());
    }

    @Override
    protected String getDialogTitle() {
        return "\u7cfb\u7edf\u5c5e\u6027\u9762\u677f";
    }
}
