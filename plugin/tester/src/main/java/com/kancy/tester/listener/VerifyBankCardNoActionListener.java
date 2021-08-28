package com.kancy.tester.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.JFrameApplicationEvent;
import com.github.kancyframework.springx.swing.action.JFrameApplicationListener;
import com.github.kancyframework.springx.swing.dialog.JTextFieldInputDialog;
import com.github.kancyframework.springx.utils.CollectionUtils;
import com.github.kancyframework.springx.utils.StringUtils;
import com.kancy.tester.data.CardBinData;
import com.kancy.tester.domain.CardBin;
import com.kancy.tester.ui.BankCardPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * VerifyBankCardNoActionListener
 *
 * @author huangchengkang
 * @date 2021/8/27 18:51
 */
@Action({"验证银行卡号码"})
@Component
public class VerifyBankCardNoActionListener extends JFrameApplicationListener {
    @Autowired
    private BankCardPanel bankCardPanel;
    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(JFrameApplicationEvent event) {
        String bankCardNo = Swing.getInput(JTextFieldInputDialog.class, bankCardPanel,"请输入需要验证的银行卡号码");
        if (Objects.isNull(bankCardNo)){
            return;
        }
        if (StringUtils.isBlank(bankCardNo)){
            Swing.msg(bankCardPanel, "请先输入银行卡号码！");
            return;
        }

        // 验证校验码
        char bankCardCheckCode = bankCardPanel.getBankCardService().getBankCardCheckCode(bankCardNo.substring(0, bankCardNo.length() - 1));
        if (!Objects.equals(String.valueOf(bankCardCheckCode), bankCardNo.substring(bankCardNo.length()-1))){
            Swing.msg(bankCardPanel, "银行卡号码[{}]格式不正确！", bankCardNo);
            return;
        }

        List<String> possibleBins = new ArrayList<>();
        for (int i = 10; i >= 2; i--) {
            possibleBins.add(bankCardNo.substring(0, i));
        }

        List<CardBin> cardBins = new ArrayList<>();
        for (String possibleBin : possibleBins) {
            CardBin cardBin = CardBinData.randomCardBin(possibleBin);
            if (Objects.nonNull(cardBin)){
                cardBins.add(cardBin);
            }
        }

        if (CollectionUtils.isEmpty(cardBins)){
            Swing.msg(bankCardPanel, "猜测银行卡[{}]卡bin不正确！", bankCardNo);
            return;
        }

        Optional<CardBin> optional = cardBins.stream()
                .filter(cardBin -> Objects.equals(cardBin.getCardLength(), bankCardNo.length()))
                .findFirst();
        if (!optional.isPresent()){
            Swing.msg(bankCardPanel,"猜测银行卡[{}]长度与卡bin不匹配！", bankCardNo);
            return;
        }

        CardBin cardBin = optional.get();

        Swing.msg(bankCardPanel, "<html>银行卡号码[<font color=green>{}</font>]有效！<br/><br/>" +
                        "银行名称：{}<br/>" +
                        "卡名称：{}<br/>" +
                        "卡类型：{}<br/>" +
                        "卡长度：{}<br/>" +
                        "卡bin：{}<br/>" +
                        "</html>", bankCardNo.trim(),
                cardBin.getBankName(),cardBin.getCardName(),
                Objects.equals(cardBin.getCardType(), "DEBIT") ? "储蓄卡" : "信用卡"
                ,bankCardNo.length(),cardBin.getId()
        );
    }


}
