package com.github.kancyframework.springx.demo.listener;

import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.demo.ui.DemoFrame;
import com.github.kancyframework.springx.swing.action.AbstractActionApplicationListener;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.ActionApplicationEvent;
import com.github.kancyframework.springx.swing.exception.AlertException;
import com.github.kancyframework.springx.utils.Assert;

import java.util.Random;

@Action({"异常测试"})
@Component
public class ExceptionTestActionListener extends AbstractActionApplicationListener<ActionApplicationEvent<DemoFrame>> {
    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ActionApplicationEvent<DemoFrame> event) {
        if (new Random().nextBoolean()){
            Assert.isTrue(false, "Assert异常测试");
        }else {
            throw new AlertException("AlertException异常测试");
        }
    }
}
