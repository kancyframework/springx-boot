package com.kancy.tester.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.JFrameApplicationEvent;
import com.github.kancyframework.springx.swing.action.JFrameApplicationListener;
import com.kancy.spring.minidb.MapDb;
import com.kancy.tester.ui.IdCardPanel;
import com.kancy.tester.utils.IDCardUtils;

/**
 * SettingResetActionListener
 *
 * @author huangchengkang
 * @date 2021/8/27 18:51
 */
@Action({"设置重置"})
@Component
public class SettingResetActionListener extends JFrameApplicationListener {
    @Autowired
    private IdCardPanel idCardPanel;
    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(JFrameApplicationEvent event) {
        MapDb.clearAll();
        IDCardUtils.setAddressEnabled(null,null,null);
        IDCardUtils.setSexEnabled(true, true);
        IDCardUtils.setAgeRange(18, 60);
        Swing.msg(idCardPanel, "重置成功！");
    }
}
