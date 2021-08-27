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
import com.kancy.tester.ui.BankCardPanel;

/**
 * SettingDefaultNationActionListener
 *
 * @author huangchengkang
 * @date 2021/8/27 18:51
 */
@Action({"复制银行卡信息"})
@KeyStroke("alt 2")
@Component
public class CopyBankCardInfoActionListener extends JFrameApplicationListener {
    @Autowired
    private BankCardPanel bankCardPanel;
    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(JFrameApplicationEvent event) {
        String bankCardNo = bankCardPanel.getBankCardNoLabel().getText();
        if (StringUtils.isBlank(bankCardNo)){
            Swing.msg(event.getSource(), "银行卡号码为空，请先生成！");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("银行名称：").append(bankCardPanel.getBankNameLabel().getText().replace(" ", "")).append("\r\n");
        sb.append("卡号：").append(bankCardNo.replace(" ", "")).append("\r\n");

        String text = bankCardPanel.getBankCardNameLabel().getText();
        String[] strings = text.split("  ");
        sb.append("卡名称：").append(strings[0]).append("\r\n");
        sb.append("卡类型：").append(strings[1].replace(" CARD", "")).append("\r\n");

        sb.append("卡长度：").append(bankCardNo.replace(" ", "").length()).append("\r\n");
        sb.append("卡bin：").append(bankCardPanel.getCardBinLabel().getText()).append("\r\n");
        SystemUtils.setClipboardText(sb.toString());
        Swing.msg(bankCardPanel, "复制成功！");
    }
}
