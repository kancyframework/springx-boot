package com.github.kancyframework.springx.swing;

import com.github.kancyframework.springx.annotation.Order;
import com.github.kancyframework.springx.boot.ApplicationRunner;
import com.github.kancyframework.springx.log.Logger;
import com.github.kancyframework.springx.log.LoggerFactory;
import com.github.kancyframework.springx.swing.action.ActionApplicationListener;
import com.github.kancyframework.springx.swing.action.SpringActionListener;
import com.github.kancyframework.springx.swing.utils.PopupMenuUtils;
import com.github.kancyframework.springx.utils.*;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * SwingApplication
 *
 * @author kancy
 * @date 2021/1/9 0:50
 */
@Order(Integer.MAX_VALUE)
public interface SwingApplication<T extends JFrame> extends ApplicationRunner, SpringActionListener {
    /**
     * run
     * @param args
     * @throws Exception
     */
    @Override
    default void run(String[] args) throws Exception {
        Logger log = LoggerFactory.getLogger(SwingApplication.class);
        Class<JFrame> jFrameClass = ClassUtils.getInterfaceGenericType(getClass());
        JFrame frame = SpringUtils.getBean(jFrameClass);
        Assert.notNull(frame, String.format("%s Is Not Spring Bean.", getClass().getName()));

        Map<String, ActionApplicationListener> actionMap = SpringUtils.getBeansOfType(ActionApplicationListener.class);
        if (CollectionUtils.isEmpty(actionMap)){
            return;
        }
        ReflectionUtils.doWithFields(jFrameClass, field -> {
            if (Component.class.isAssignableFrom(field.getType())
                && !TextComponent.class.isAssignableFrom(field.getType())
                && !JTextComponent.class.isAssignableFrom(field.getType())
            ){
                ReflectionUtils.makeAccessible(field);
                Class<?> fieldType = field.getType();
                Object fieldValue = field.get(frame);

                if (Objects.isNull(fieldValue)){
                    return;
                }

                Method method = ReflectionUtils.findMethod(fieldType, "addActionListener", ActionListener.class);
                if (Objects.nonNull(method)){
                    try {
                        Method getActionListeners = ReflectionUtils.findMethod(fieldType, "getActionListeners");
                        ActionListener[] actionListeners = (ActionListener[]) getActionListeners.invoke(fieldValue);
                        long springActionListenerSize = Arrays.stream(actionListeners)
                                .filter(ac -> SpringActionListener.class.isAssignableFrom(ac.getClass()))
                                .count();
                        if (actionListeners.length == 0 && springActionListenerSize == 0){
                            log.info("成功添加SpringActionListener：{}({})",field.getType().getSimpleName(), field.getName());
                            method.invoke(fieldValue, this);
                        }
                    } catch (InvocationTargetException e) {
                        throw new IllegalAccessException(e.getMessage());
                    }
                }

                // 菜单类
                if (MenuElement.class.isAssignableFrom(field.getType())){
                    List<AbstractButton> abstractButtons = PopupMenuUtils.findAbstractButtons(MenuElement.class.cast(fieldValue));
                    for (AbstractButton button : abstractButtons) {
                        long springActionListenerSize = Arrays.stream(button.getActionListeners())
                                .filter(ac -> SpringActionListener.class.isAssignableFrom(ac.getClass()))
                                .count();
                        if (button.getActionListeners().length == 0 && springActionListenerSize == 0){
                            log.info("成功添加SpringActionListener：MenuElement({}-{})",button.getClass().getSimpleName(), button.getText());
                            button.addActionListener(this);
                            // 快捷键设置
                            List<ActionApplicationListener> actionList = actionMap.values().stream()
                                    .filter(action -> action.isSupport(button.getActionCommand()))
                                    .collect(Collectors.toList());
                            if (actionList.size() == 1){
                                ActionApplicationListener actionApplicationListener = actionList.get(0);
                                KeyStroke accelerator = actionApplicationListener.getAccelerator(button.getActionCommand());
                                if (Objects.nonNull(accelerator)){
                                    Object keyStroke = null;
                                    Method getAccelerator = ReflectionUtils.findMethod(button.getClass(), "getAccelerator");
                                    if (Objects.nonNull(getAccelerator)){
                                        keyStroke = ReflectionUtils.invokeMethod(getAccelerator, button);
                                    }
                                    if (Objects.nonNull(keyStroke)){
                                        continue;
                                    }

                                    Method setAccelerator = ReflectionUtils.findMethod(button.getClass(), "setAccelerator", KeyStroke.class);
                                    if (Objects.nonNull(setAccelerator)){
                                        ReflectionUtils.invokeMethod(setAccelerator, button, accelerator);
                                        log.info("成功设置MenuElement({}-{})快捷键成功：{}", button.getClass().getSimpleName(), button.getText(), accelerator);
                                    }
                                }
                            }else {
                                log.debug("无法给存在多个监听器的MenuElement({}-{})设置快捷键！", button.getClass().getSimpleName(), button.getText());
                            }
                        }
                    }
                }
            }
        });
        log.info("SpringActionListener autoconfigure completed!");
        Swing.startUp(frame);
        customSettings((T) frame);
    }

    /**
     * 自定义设置
     * @param frame frame
     */
    default void customSettings(T frame){

    }


    @Override
    default Object getSource(String actionCommand) {
        return SpringUtils.getBean(ClassUtils.getInterfaceGenericType(getClass()));
    }
}
