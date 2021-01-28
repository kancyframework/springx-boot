package com.github.kancyframework.springx.swing.action;

import com.github.kancyframework.springx.context.event.ApplicationEvent;

import java.awt.event.ActionEvent;

/**
 * ActionApplicationEvent
 *
 * @author kancy
 * @date 2020/12/13 1:18
 */
public class ActionApplicationEvent<T> extends ApplicationEvent<T> {
    private ActionEvent actionEvent;
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ActionApplicationEvent(T source) {
        super(source);
    }

    public ActionApplicationEvent(T source, ActionEvent actionEvent) {
        super(source);
        setActionEvent(actionEvent);
    }

    public void setActionEvent(ActionEvent actionEvent) {
        this.actionEvent = actionEvent;
    }

    public ActionEvent getActionEvent() {
        return actionEvent;
    }
}
