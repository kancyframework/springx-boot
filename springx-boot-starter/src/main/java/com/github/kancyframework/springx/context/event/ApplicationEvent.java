package com.github.kancyframework.springx.context.event;

import java.util.EventObject;

/**
 * ApplicationEvent
 *
 * @author kancy
 * @date 2020/2/18 11:22
 */
public abstract class ApplicationEvent<T> extends EventObject {

    private static final long serialVersionUID = 5516075349620653480L;

    private final long timestamp = System.currentTimeMillis();

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ApplicationEvent(T source) {
        super(source);
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public T getSource() {
        return (T) source;
    }
}