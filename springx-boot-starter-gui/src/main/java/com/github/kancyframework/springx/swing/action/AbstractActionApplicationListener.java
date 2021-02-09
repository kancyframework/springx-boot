package com.github.kancyframework.springx.swing.action;

import com.github.kancyframework.springx.utils.CollectionUtils;
import com.github.kancyframework.springx.utils.StringUtils;

import java.awt.event.ActionEvent;
import java.util.*;

/**
 * AbstractActionApplicationListener
 *
 * @author kancy
 * @date 2020/12/13 1:03
 */
public abstract class AbstractActionApplicationListener<E extends ActionApplicationEvent>
        implements ActionApplicationListener<E> {
    /**
     * 缓存这个值，避免每次解析
     */
    private static final Map<Class<? extends AbstractActionApplicationListener>, Set<String>> CACHE = new HashMap<>();

    /**
     * 是否支持
     *
     * @param event
     * @return
     */
    @Override
    public boolean isSupport(ActionEvent event) {
        return isSupport(event.getActionCommand());
    }

    /**
     * 是否支持
     *
     * @param actionCommand
     * @return
     */
    @Override
    public boolean isSupport(String actionCommand) {
        return getSupportActionCommandSet().contains(actionCommand);
    }

    public Set<String> getSupportActionCommandSet() {
        if (!CACHE.containsKey(this.getClass())) {
            synchronized (CACHE) {
                if (!CACHE.containsKey(this.getClass())) {
                    CACHE.put(this.getClass(), initActionCommandSet());
                }
            }
        }
        return CACHE.getOrDefault(getClass(), Collections.emptySet());
    }

    /**
     * 提供支持的ActionCommand,逗号分隔
     *
     * @return
     */
    @Override
    public String getSupportActionCommands() {
        return StringUtils.join(getSupportActionCommandSet().toArray(new String[0]), ",");
    }

    /**
     * 初始化
     */
    private Set<String> initActionCommandSet() {
        // 用注解的方式
        Action action = this.getClass().getDeclaredAnnotation(Action.class);
        if (Objects.nonNull(action)){
            return CollectionUtils.toSet(action.value());
        }
        ActionCommand actionCommand = this.getClass().getDeclaredAnnotation(ActionCommand.class);
        if (Objects.nonNull(actionCommand)){
            return CollectionUtils.toSet(actionCommand.value());
        }
        return Collections.emptySet();
    }


    /**
     * 快捷键
     * 例如：ctrl alt shift 1 - > KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.CTRL_MASK|KeyEvent.ALT_MASK|KeyEvent.SHIFT_MASK)
     * @param actionCommand
     * @return
     */
    @Override
    public javax.swing.KeyStroke getAccelerator(String actionCommand) {
        Set<String> actions = CACHE.get(getClass());
        if (Objects.nonNull(actions) && actions.size() > 1){
            return null;
        }

        Accelerator annotation = this.getClass().getDeclaredAnnotation(Accelerator.class);
        if (Objects.nonNull(annotation)){
            String accelerator = annotation.value();
            if (annotation.keyCode() > 0 && annotation.modifiers() > 0){
                return javax.swing.KeyStroke.getKeyStroke(annotation.keyCode(), annotation.modifiers());
            }
            if (StringUtils.isNotBlank(accelerator)){
                return javax.swing.KeyStroke.getKeyStroke(accelerator);
            }
        }
        KeyStroke keyStroke = this.getClass().getDeclaredAnnotation(KeyStroke.class);
        if (Objects.nonNull(keyStroke)){
            String accelerator = keyStroke.value();
            if (keyStroke.keyCode() > 0 && keyStroke.modifiers() > 0){
                return javax.swing.KeyStroke.getKeyStroke(keyStroke.keyCode(), keyStroke.modifiers());
            }
            if (StringUtils.isNotBlank(accelerator)){
                return javax.swing.KeyStroke.getKeyStroke(accelerator);
            }
        }
        return null;
    }

}
