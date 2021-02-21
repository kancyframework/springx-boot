package com.github.kancyframework.springx.demo.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.action.AbstractActionApplicationListener;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.ActionApplicationEvent;
import com.github.kancyframework.springx.swing.action.KeyStroke;
import com.github.kancyframework.springx.swing.dialog.SystemPropertiesDialog;

import javax.swing.*;

@Component
@KeyStroke("ctrl shift alt F2")
@Action(value = "系统属性")
public class SystemPropertiesListener extends AbstractActionApplicationListener<ActionApplicationEvent> {

    @Autowired
    private JFrame frame;

    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ActionApplicationEvent event) {
        SystemPropertiesDialog dialog = new SystemPropertiesDialog(frame);
        dialog.setVisible(true);
    }
}
