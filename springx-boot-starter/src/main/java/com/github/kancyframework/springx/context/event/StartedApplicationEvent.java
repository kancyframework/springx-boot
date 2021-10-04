package com.github.kancyframework.springx.context.event;

import com.github.kancyframework.springx.context.ApplicationContext;

/**
 * StartedApplicationEvent
 *
 * @author huangchengkang
 * @date 2021/9/16 23:21
 */
public class StartedApplicationEvent extends ApplicationEvent<ApplicationContext>{
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public StartedApplicationEvent(ApplicationContext source) {
        super(source);
    }
}
