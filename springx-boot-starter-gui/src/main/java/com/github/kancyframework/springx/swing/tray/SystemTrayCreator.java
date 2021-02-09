package com.github.kancyframework.springx.swing.tray;

import com.github.kancyframework.springx.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * 系统托盘创建器
 */
public interface SystemTrayCreator {
    /**
     * 获取托盘菜单
     * 乱码问题：-Dfile.encoding=gbk
     * @param frame
     * @return
     */
    default PopupMenu getPopupMenu(JFrame frame){
        PopupMenu popupMenu = new PopupMenu();
        MenuItem openItem = new MenuItem("打开");
        MenuItem exitItem = new MenuItem("退出");
        openItem.addActionListener(e -> {
            if (!frame.isVisible()){
                frame.setVisible(true);
            } else {
                frame.requestFocusInWindow();
            }
        });
        exitItem.addActionListener(e -> System.exit(0));
        popupMenu.add(openItem);
        popupMenu.add(exitItem);
        return popupMenu;
    }

    /**
     * 获取托盘图标
     * @param frame
     * @return
     */
    default Image getImage(JFrame frame){
        Image iconImage = frame.getIconImage();
        if (Objects.isNull(iconImage)) {
            iconImage = ((ImageIcon)UIManager.getIcon("InternalFrame.icon")).getImage();
        }
        return iconImage;
    }

    /**
     * 获取托盘提示
     * @param frame
     * @return
     */
    default String getTooltip(JFrame frame){
        return StringUtils.isNotBlank(frame.getTitle()) ? frame.getTitle() : "我是一个小工具！";
    }
}
