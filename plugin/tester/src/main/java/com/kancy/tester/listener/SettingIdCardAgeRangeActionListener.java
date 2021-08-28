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
import com.kancy.tester.utils.IDCardUtils;

/**
 * SettingDefaultNationActionListener
 *
 * @author huangchengkang
 * @date 2021/8/27 18:51
 */
@Action({"设置年龄区间"})
@KeyStroke("ctrl 3")
@Component
public class SettingIdCardAgeRangeActionListener extends JFrameApplicationListener {
    @Autowired
    private IdCardPanel idCardPanel;
    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(JFrameApplicationEvent event) {
        String idCardAgeRange = Swing.getInput(JTextFieldInputDialog.class, null,"设置年龄区间（格式：年龄1-年龄2）",
                MapDb.getData("idCardAgeRange", "18-50"));

        if (StringUtils.isBlank(idCardAgeRange)){
            return;
        }

        String[] strings = StringUtils.toArray(idCardAgeRange, "-");
        IDCardUtils.setAgeRange(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]));
        MapDb.putData("idCardAgeRange", idCardAgeRange);
        Swing.msg(idCardPanel, "设置成功！");
    }
}
