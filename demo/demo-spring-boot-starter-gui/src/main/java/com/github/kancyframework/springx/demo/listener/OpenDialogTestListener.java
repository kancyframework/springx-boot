package com.github.kancyframework.springx.demo.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.context.event.ApplicationEvent;
import com.github.kancyframework.springx.demo.ui.DialogTest;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.action.AbstractActionApplicationListener;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.KeyStroke;

@Component
@KeyStroke("alt 1")
@Action(value = "打开DialogTest")
public class OpenDialogTestListener extends AbstractActionApplicationListener {
    @Autowired
    private DialogTest dialog;
    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        Swing.visible(dialog);
    }
}
