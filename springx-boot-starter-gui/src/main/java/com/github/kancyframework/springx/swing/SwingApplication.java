package com.github.kancyframework.springx.swing;

import com.github.kancyframework.springx.boot.ApplicationRunner;
import com.github.kancyframework.springx.annotation.Order;
import com.github.kancyframework.springx.log.Logger;
import com.github.kancyframework.springx.log.LoggerFactory;
import com.github.kancyframework.springx.swing.action.SpringActionListener;
import com.github.kancyframework.springx.utils.Assert;
import com.github.kancyframework.springx.utils.ClassUtils;
import com.github.kancyframework.springx.utils.ReflectionUtils;
import com.github.kancyframework.springx.utils.SpringUtils;
import com.github.kancyframework.springx.swing.utils.PopupMenuUtils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

        ReflectionUtils.doWithFields(jFrameClass, field -> {
            if (Component.class.isAssignableFrom(field.getType())
                && !TextComponent.class.isAssignableFrom(field.getType())
                && !JTextComponent.class.isAssignableFrom(field.getType())
            ){
                ReflectionUtils.makeAccessible(field);
                Class<?> fieldType = field.getType();
                Object fieldValue = field.get(frame);
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
