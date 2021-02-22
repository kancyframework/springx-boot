package com.github.kancyframework.springx.demo.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.context.event.ApplicationEvent;
import com.github.kancyframework.springx.demo.ui.DialogTest;
import com.github.kancyframework.springx.demo.ui.FrameTest;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.action.AbstractActionApplicationListener;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.KeyStroke;

@Component
@KeyStroke("alt 2")
@Action(value = "打开FrameTest")
public class OpenFrameTestListener extends AbstractActionApplicationListener {
    @Autowired
    private FrameTest frameTest;
    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        Swing.visible(frameTest);
    }
}
