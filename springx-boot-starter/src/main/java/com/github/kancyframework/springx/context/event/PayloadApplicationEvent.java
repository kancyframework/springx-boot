package com.github.kancyframework.springx.context.event;

/**
 * PayloadApplicationEvent
 *
 * @author kancy
 * @date 2020/2/18 13:16
 */
public class PayloadApplicationEvent<T> extends ApplicationEvent {
    private T payload;

    public PayloadApplicationEvent(Object source, T payload) {
        super(source);
        this.payload = payload;
    }

    public PayloadApplicationEvent(T payload) {
        super(payload);
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }
}
