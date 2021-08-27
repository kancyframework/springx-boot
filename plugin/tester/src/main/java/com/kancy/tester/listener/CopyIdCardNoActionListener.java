package com.kancy.tester.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.JFrameApplicationEvent;
import com.github.kancyframework.springx.swing.action.JFrameApplicationListener;
import com.github.kancyframework.springx.swing.action.KeyStroke;
import com.github.kancyframework.springx.swing.utils.SystemUtils;
import com.github.kancyframework.springx.utils.StringUtils;
import com.kancy.tester.ui.IdCardPanel;

/**
 * SettingDefaultNationActionListener
 *
 * @author huangchengkang
 * @date 2021/8/27 18:51
 */
@Action({"复制身份证号码"})
@KeyStroke("alt 1")
@Component
public class CopyIdCardNoActionListener extends JFrameApplicationListener {
    @Autowired
    private IdCardPanel idCardPanel;
    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(JFrameApplicationEvent event) {
        String idCardNo = idCardPanel.getIdCardNoTextField().getText();
        if (StringUtils.isBlank(idCardNo)){
            Swing.msg(event.getSource(), "身份证号码为空，请先生成！");
            return;
        }
        SystemUtils.setClipboardText(idCardNo);
    }
}
