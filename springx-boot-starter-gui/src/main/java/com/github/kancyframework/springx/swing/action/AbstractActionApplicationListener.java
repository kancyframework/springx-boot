package com.github.kancyframework.springx.swing.action;

import com.github.kancyframework.springx.utils.CollectionUtils;

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
        String actionCommand = event.getActionCommand();
        if (!CACHE.containsKey(this.getClass())){
            synchronized (CACHE){
                if (!CACHE.containsKey(this.getClass())){
                    CACHE.put(this.getClass(), initActionCommandSet());
                }
            }
        }
        return CACHE.get(this.getClass()).contains(actionCommand);
    }

    /**
     * 初始化
     */
    private Set<String> initActionCommandSet() {
        // 用注解的方式
        Action annotation = this.getClass().getDeclaredAnnotation(Action.class);
        if (Objects.nonNull(annotation)){
            return CollectionUtils.toSet(annotation.value());
        }
        return Collections.emptySet();
    }
}
