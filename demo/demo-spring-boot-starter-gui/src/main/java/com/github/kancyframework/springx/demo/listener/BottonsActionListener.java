package com.github.kancyframework.springx.demo.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.demo.ui.DemoFrame;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.action.AbstractActionApplicationListener;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.ActionApplicationEvent;
import com.kancy.spring.minidb.MapDb;

import javax.swing.*;
import java.awt.event.ActionEvent;

@Action({"落","花","不","是","无","情","物"})
@Component
public class BottonsActionListener extends AbstractActionApplicationListener<ActionApplicationEvent<DemoFrame>> {

    @Autowired
    private JFrame frame;

    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ActionApplicationEvent<DemoFrame> event) {
        ActionEvent actionEvent = event.getActionEvent();
        Object source = actionEvent.getSource();
        if (source instanceof JButton){
            String text = JButton.class.cast(source).getText();
            MapDb.putData(text, MapDb.getData(text, 0) + 1);
            Swing.msg(event.getSource(), String.format("落花不是无情物：%s", text));
        }
    }

}
