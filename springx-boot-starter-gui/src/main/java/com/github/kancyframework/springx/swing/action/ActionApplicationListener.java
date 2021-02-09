package com.github.kancyframework.springx.swing.action;

import com.github.kancyframework.springx.context.event.ApplicationEvent;
import com.github.kancyframework.springx.context.event.ApplicationListener;
import com.github.kancyframework.springx.utils.StringUtils;

import java.awt.event.ActionEvent;

/**
 * ActionApplicationListener
 *
 * @author kancy
 * @date 2020/12/13 1:00
 */
public interface ActionApplicationListener<E extends ApplicationEvent> extends ApplicationListener<E> {


    /**
     * 是否支持
     * @param event
     * @return
     */
    default boolean isSupport(ActionEvent event){
        return isSupport(event.getActionCommand());
    }

    /**
     * 是否支持
     * @param actionCommand
     * @return
     */
    default boolean isSupport(String actionCommand){
        return StringUtils.toSet(getSupportActionCommands()).contains(actionCommand);
    }

    /**
     * 提供支持的ActionCommand,逗号分隔
     * @return
     */
    default String getSupportActionCommands(){
        return StringUtils.empty();
    }

    /**
     * 快捷键
     * 例如：ctrl alt shift 1 - > KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.CTRL_MASK|KeyEvent.ALT_MASK|KeyEvent.SHIFT_MASK)
     * @param actionCommand
     * @return
     */
    default javax.swing.KeyStroke getAccelerator(String actionCommand) {
        return null;
    }
}
