package com.github.kancyframework.springx.context.event;

/**
 * ApplicationEventPublisher
 *
 * @author kancy
 * @date 2020/2/18 12:07
 */
public interface ApplicationEventPublisher {
    /**
     * 事件发布
     * @param applicationEvent
     */
    void publishEvent(ApplicationEvent applicationEvent);
}
