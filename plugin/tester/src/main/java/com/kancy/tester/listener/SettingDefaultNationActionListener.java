package com.kancy.tester.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.JFrameApplicationEvent;
import com.github.kancyframework.springx.swing.action.JFrameApplicationListener;
import com.github.kancyframework.springx.swing.action.KeyStroke;
import com.github.kancyframework.springx.swing.dialog.JTextFieldInputDialog;
import com.github.kancyframework.springx.utils.StringUtils;
import com.kancy.spring.minidb.MapDb;
import com.kancy.tester.ui.IdCardPanel;

/**
 * SettingDefaultNationActionListener
 *
 * @author huangchengkang
 * @date 2021/8/27 18:51
 */
@Action({"设置默认民族"})
@KeyStroke("ctrl 1")
@Component
public class SettingDefaultNationActionListener extends JFrameApplicationListener {
    @Autowired
    private IdCardPanel idCardPanel;
    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(JFrameApplicationEvent event) {
        String nation = Swing.getInput(JTextFieldInputDialog.class, null,"设置默认民族",
                MapDb.getData("defaultNation", "汉"));
        if (StringUtils.isBlank(nation)){
            return;
        }
        idCardPanel.getImageNationLabel().setText(nation);
        MapDb.putData("defaultNation", nation);
        Swing.msg(idCardPanel, "设置成功！");
    }
}
