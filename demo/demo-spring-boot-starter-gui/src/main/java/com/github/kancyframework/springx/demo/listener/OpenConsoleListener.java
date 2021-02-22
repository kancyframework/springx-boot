package com.github.kancyframework.springx.demo.listener;

import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.action.AbstractActionApplicationListener;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.ActionApplicationEvent;
import com.github.kancyframework.springx.swing.action.KeyStroke;
import com.github.kancyframework.springx.swing.console.ConsoleDialog;
import com.github.kancyframework.springx.swing.tray.SystemTrayMenuProvider;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;

@Component
@KeyStroke("ctrl 1")
@Action(value = "控制台")
public class OpenConsoleListener extends AbstractActionApplicationListener<ActionApplicationEvent<JFrame>> implements SystemTrayMenuProvider {

    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ActionApplicationEvent event) {
        ConsoleDialog.open();
    }

    /**
     * 设置托盘菜单
     *
     * @param popupMenu
     * @param frame
     */
    @Override
    public void setMenu(PopupMenu popupMenu, JFrame frame) {
        MenuItem menuItem = new MenuItem("控制台（额外）");
        menuItem.setActionCommand("控制台");
        popupMenu.add(menuItem);
    }
}
