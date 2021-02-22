package com.github.kancyframework.springx.demo.listener;

import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.action.AbstractActionApplicationListener;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.ActionApplicationEvent;
import com.github.kancyframework.springx.swing.action.KeyStroke;
import com.github.kancyframework.springx.swing.dialog.PropertiesDialog;
import com.kancy.spring.minidb.MapDb;

import javax.annotation.Resource;
import javax.swing.*;

@Component
@KeyStroke("ctrl shift alt F2")
@Action(value = "缓存属性")
public class MapDbPropertiesListener extends AbstractActionApplicationListener<ActionApplicationEvent> {

    @Resource
    private JFrame frame;

    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ActionApplicationEvent event) {
        PropertiesDialog dialog = new PropertiesDialog(frame, MapDb.get().getProperties());
        dialog.setVisible(true);
    }
}
