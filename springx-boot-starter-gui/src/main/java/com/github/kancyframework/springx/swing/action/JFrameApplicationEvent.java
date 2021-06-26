package com.github.kancyframework.springx.swing.action;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * JFrameApplicationEvent
 *
 * @author Administrator
 * @date 2021/6/26 19:54
 */
public class JFrameApplicationEvent extends ActionApplicationEvent<JFrame> {
    public JFrameApplicationEvent(JFrame source) {
        super(source);
    }

    public JFrameApplicationEvent(JFrame source, ActionEvent actionEvent) {
        super(source, actionEvent);
    }
}
