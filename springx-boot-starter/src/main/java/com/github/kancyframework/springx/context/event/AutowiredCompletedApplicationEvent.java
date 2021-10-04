package com.github.kancyframework.springx.context.event;

import com.github.kancyframework.springx.context.ApplicationContext;

/**
 * PostInitializeApplicationEvent
 *
 * @author huangchengkang
 * @date 2021/9/16 23:23
 */
public class AutowiredCompletedApplicationEvent extends ApplicationEvent<ApplicationContext>{
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public AutowiredCompletedApplicationEvent(ApplicationContext source) {
        super(source);
    }
}
