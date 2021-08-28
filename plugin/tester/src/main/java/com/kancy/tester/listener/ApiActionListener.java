package com.kancy.tester.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.JFrameApplicationEvent;
import com.github.kancyframework.springx.swing.action.JFrameApplicationListener;
import com.github.kancyframework.springx.swing.utils.SystemUtils;
import com.github.kancyframework.springx.utils.StringUtils;
import com.kancy.tester.ui.IdCardPanel;

import java.util.Objects;

/**
 * ApiActionListener
 *
 * @author huangchengkang
 * @date 2021/8/29 1:56
 */
@Action({"银行卡影像接口","银行卡数据接口","身份证影像接口","身份证数据接口"})
@Component
public class ApiActionListener extends JFrameApplicationListener {
    @Autowired
    private IdCardPanel idCardPanel;

    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(JFrameApplicationEvent event) {
        String baseUrl = "http://localhost:5656/%s";
        String actionCommand = event.getActionEvent().getActionCommand();

        String url = "";
        if (Objects.equals(actionCommand, "银行卡影像接口")){
            url = String.format(baseUrl, "image?type=bankcard");
        }else if (Objects.equals(actionCommand, "银行卡数据接口")){
            url = String.format(baseUrl, "data?type=bankcard&cardType=debit,credit&bankName=中国银行");
        }if (Objects.equals(actionCommand, "身份证影像接口")){
            url = String.format(baseUrl, "image?type=idcard");
        }if (Objects.equals(actionCommand, "身份证数据接口")){
            url = String.format(baseUrl, "data?type=idcard");
        }

        if (StringUtils.isNotBlank(url)){
            SystemUtils.openBrowser(url);
        }
    }
}