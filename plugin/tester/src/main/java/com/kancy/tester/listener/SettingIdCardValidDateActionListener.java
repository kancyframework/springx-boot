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

import java.util.Objects;

/**
 * SettingDefaultNationActionListener
 *
 * @author huangchengkang
 * @date 2021/8/27 18:51
 */
@Action({"设置身份证有效期"})
@KeyStroke("ctrl 5")
@Component
public class SettingIdCardValidDateActionListener extends JFrameApplicationListener {
    @Autowired
    private IdCardPanel idCardPanel;
    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(JFrameApplicationEvent event) {
        String cardValidDate = Swing.getInput(JTextFieldInputDialog.class, idCardPanel,
                "设置默认身份证有效期（yyyy.MM.dd-yyyy.MM.dd/长期）",
                MapDb.getData("defaultIdCardValidDate", ""));
        if (Objects.isNull(cardValidDate)){
            return;
        }
        if (StringUtils.isBlank(cardValidDate)){
            MapDb.putData("defaultIdCardValidDate", cardValidDate);
            Swing.msg(idCardPanel, "已清除默认身份证有效期！");
            return;
        } else {
            cardValidDate = cardValidDate.trim();
            idCardPanel.getImageCardValidDateLabel().setText(cardValidDate);
        }
        MapDb.putData("defaultIdCardValidDate", cardValidDate);
        Swing.msg(idCardPanel, "设置成功！");
    }
}
