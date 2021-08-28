package com.kancy.tester.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.JFrameApplicationEvent;
import com.github.kancyframework.springx.swing.action.JFrameApplicationListener;
import com.kancy.tester.ui.IdCardPanel;

/**
 * AboutActionListener
 *
 * @author huangchengkang
 * @date 2021/8/27 18:51
 */
@Action({"关于"})
@Component
public class AboutActionListener extends JFrameApplicationListener {
    @Autowired
    private IdCardPanel idCardPanel;
    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(JFrameApplicationEvent event) {
        Swing.msg(idCardPanel, "<html>Api服务 : http://localhost:5656 <br/> " +
                "数据接口：<a href='http://localhost:5656/data?type=idcard'>身份证</a>、" +
                "<a href='http://localhost:5656/data?type=bankcard'>银行卡</a><br/>" +
                "影像接口：<a href='http://localhost:5656/image?type=idcard'>身份证</a>、" +
                "<a href='http://localhost:5656/image?type=bankcard'>银行卡</a><br/>" +
                "</html>");
    }
}
