package com.kancy.tester.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.JFrameApplicationEvent;
import com.github.kancyframework.springx.swing.action.JFrameApplicationListener;
import com.kancy.tester.ui.IdCardPanel;
import com.kancy.tester.utils.IDCardUtils;

/**
 * SettingSexActionListener
 *
 * @author huangchengkang
 * @date 2021/8/28 2:26
 */
@Action({"设置生成性别"})
@Component
public class SettingSexActionListener extends JFrameApplicationListener {
    @Autowired
    private IdCardPanel idCardPanel;
    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(JFrameApplicationEvent event) {
        IDCardUtils.setSexEnabled(idCardPanel.getSexBoyCheckBoxMenuItem().isSelected(),
                idCardPanel.getSexGirlCheckBoxMenuItem().isSelected());
    }
}
