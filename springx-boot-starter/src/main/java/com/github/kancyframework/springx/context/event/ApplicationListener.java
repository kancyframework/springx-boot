package com.github.kancyframework.springx.context.event;

import java.util.EventListener;

/**
 * ApplicationEvent
 *
 * @author kancy
 * @date 2020/2/18 11:22
 */
@FunctionalInterface
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {
    /**
     * 处理事件
     * @param event
     */
    void onApplicationEvent(E event);
}
