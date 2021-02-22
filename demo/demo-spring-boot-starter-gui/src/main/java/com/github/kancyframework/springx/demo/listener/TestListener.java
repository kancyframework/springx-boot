package com.github.kancyframework.springx.demo.listener;

import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.context.event.ApplicationEvent;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.action.AbstractActionApplicationListener;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.KeyStroke;

@Component
@KeyStroke("ctrl 0")
@Action(value = "test")
public class TestListener extends AbstractActionApplicationListener {

    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        Swing.msg("test!");
    }
}
