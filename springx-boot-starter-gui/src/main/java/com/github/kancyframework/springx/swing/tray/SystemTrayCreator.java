package com.github.kancyframework.springx.swing.tray;

import com.github.kancyframework.springx.annotation.Order;
import com.github.kancyframework.springx.swing.console.ConsoleDialog;
import com.github.kancyframework.springx.swing.dialog.SystemPropertiesDialog;
import com.github.kancyframework.springx.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * 默认系统托盘创建器，优先级最低
 */
@Order(Integer.MAX_VALUE)
public class SystemTrayCreator implements ActionListener {

    private final JFrame frame;

    public SystemTrayCreator(JFrame frame) {
        this.frame = frame;
    }

    /**
     * 获取托盘菜单
     * 乱码问题：-Dfile.encoding=gbk
     *
     * @return
     */
    public PopupMenu getPopupMenu() {
        PopupMenu popupMenu = new PopupMenu();
        MenuItem openItem = new MenuItem("打开");
        openItem.addActionListener(this);

        MenuItem exitItem = new MenuItem("退出");
        exitItem.addActionListener(this);

        MenuItem consoleItem = new MenuItem("控制台");
        consoleItem.addActionListener(this);

        MenuItem systemItem = new MenuItem("系统属性");
        systemItem.addActionListener(this);

        popupMenu.add(openItem);
        popupMenu.add(exitItem);
        popupMenu.addSeparator();
        popupMenu.add(consoleItem);
        popupMenu.add(systemItem);
        return popupMenu;
    }

    /**
     * 获取托盘图标
     *
     * @return
     */
    public Image getImage() {
        Image iconImage = frame.getIconImage();
        if (Objects.isNull(iconImage)) {
            iconImage = ((ImageIcon)UIManager.getIcon("InternalFrame.icon")).getImage();
        }
        return iconImage;
    }

    /**
     * 获取托盘提示
     *
     * @return
     */
    public String getTooltip() {
        return StringUtils.isNotBlank(frame.getTitle()) ? frame.getTitle() : "我是一个小工具！";
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "打开": openApp(e);break;
            case "退出": exitApp(e);break;
            case "控制台": openConsole(e);break;
            case "系统属性": openSystemProperties(e);break;
        }
    }

    private void openSystemProperties(ActionEvent e) {
        SystemPropertiesDialog dialog = new SystemPropertiesDialog(frame);
        dialog.setVisible(true);
    }

    private void openConsole(ActionEvent e) {
        ConsoleDialog.open();
    }

    private void exitApp(ActionEvent e) {
        System.exit(0);
    }

    private void openApp(ActionEvent e) {
        if (frame.isVisible()){
            frame.requestFocusInWindow();
        } else {
            frame.setVisible(true);
        }
    }
}
