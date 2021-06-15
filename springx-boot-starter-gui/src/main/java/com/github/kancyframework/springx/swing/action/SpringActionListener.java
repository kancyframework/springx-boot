package com.github.kancyframework.springx.swing.action;

import com.github.kancyframework.springx.log.Logger;
import com.github.kancyframework.springx.log.LoggerFactory;
import com.github.kancyframework.springx.swing.dialog.MessageDialog;
import com.github.kancyframework.springx.swing.exception.AlertException;
import com.github.kancyframework.springx.utils.ReflectionUtils;
import com.github.kancyframework.springx.utils.SpringUtils;
import com.github.kancyframework.springx.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * SpringActionListener
 *
 * @author kancy
 * @date 2020/12/13 1:22
 */
public interface SpringActionListener extends ActionListener {
    Logger log = LoggerFactory.getLogger(SpringActionListener.class);
    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    default void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        ActionApplicationEvent<?> event = new ActionApplicationEvent<>(getSource(actionCommand), e);
        try {
            Map<String, ActionApplicationListener> actionMap = SpringUtils.getBeansOfType(ActionApplicationListener.class);
            List<ActionApplicationListener> actionList = actionMap.values().stream()
                    .filter(action -> action.isSupport(e))
                    .collect(Collectors.toList());
            if (actionList.isEmpty()){
                Component component = null;
                if (event.getSource() instanceof Component){
                    component = Component.class.cast(event.getSource());
                }
                if (StringUtils.isNotBlank(actionCommand) && !(actionCommand.equalsIgnoreCase("null") || actionCommand.equalsIgnoreCase("empty"))){
                    MessageDialog messageDialog = new MessageDialog(component, "\u8be5\u529f\u80fd\u6682\u4e0d\u652f\u6301\uff01");
                    messageDialog.show();
                }
            } else {
                actionList.forEach(action -> action.onApplicationEvent(event));
            }
        } catch (Exception exception) {
            String message = "";
            if (exception instanceof IllegalArgumentException){
                log.warn(exception.getMessage());
                message = exception.getMessage();
                if (StringUtils.isBlank(message)){
                    message = "\u53c2\u6570\u9519\u8bef\uff01";
                }
            } else if (exception instanceof AlertException){
                log.warn(exception.getMessage());
                message = AlertException.class.cast(exception).getFriendlyMessage();
            } else {
                log.error(String.format("\u64cd\u4f5c\u5f02\u5e38\uff1a%s", exception.getMessage()), exception);
                MessageDialog messageDialog = new MessageDialog("\u64cd\u4f5c\u5f02\u5e38\uff01");
                messageDialog.show();
                return;
            }

            Component component = null;
            if (event.getSource() instanceof Component){
                component = Component.class.cast(event.getSource());
            }
            MessageDialog messageDialog = new MessageDialog(component, message);
            messageDialog.show();
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
