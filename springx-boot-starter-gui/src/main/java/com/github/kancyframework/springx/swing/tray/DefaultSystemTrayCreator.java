package com.github.kancyframework.springx.swing.tray;

import com.github.kancyframework.springx.annotation.Order;

/**
 * 默认系统托盘创建器，优先级最低
 */
@Order(Integer.MAX_VALUE)
public class DefaultSystemTrayCreator implements SystemTrayCreator {

}
