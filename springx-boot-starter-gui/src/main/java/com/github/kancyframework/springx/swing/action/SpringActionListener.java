package com.github.kancyframework.springx.swing.action;

import com.github.kancyframework.springx.utils.SpringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SpringActionListener
 *
 * @author kancy
 * @date 2020/12/13 1:22
 */
public interface SpringActionListener extends ActionListener {

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    default void actionPerformed(ActionEvent e) {
        Map<String, ActionApplicationListener> actionMap = SpringUtils.getBeansOfType(ActionApplicationListener.class);
        ActionApplicationEvent<?> event = new ActionApplicationEvent<>(getSource(e.getActionCommand()), e);
        List<ActionApplicationListener> actionList = actionMap.values().stream()
                .filter(action -> action.isSupport(e))
                .collect(Collectors.toList());
        if (actionList.isEmpty()){
            JComponent component = null;
            if (event.getSource() instanceof JComponent){
                component = JComponent.class.cast(event.getSource());
            }
            JOptionPane.showMessageDialog(component, "\u8be5\u529f\u80fd\u6682\u4e0d\u652f\u6301\uff01");
        } else {
            actionList.forEach(action -> action.onApplicationEvent(event));
        }
    }

    /**
     * 源对象
     * @return
     * @param actionCommand
     */
    default Object getSource(String actionCommand){
        return this;
    }
}
