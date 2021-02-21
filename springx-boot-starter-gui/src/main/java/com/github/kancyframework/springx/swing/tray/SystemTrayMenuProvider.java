package com.github.kancyframework.springx.swing.tray;

import javax.swing.*;
import java.awt.*;

public interface SystemTrayMenuProvider {
    /**
     * 设置托盘菜单
     * @param popupMenu
     * @param frame
     */
    void setMenu(PopupMenu popupMenu, JFrame frame);
}
