package com.kancy.tester.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.JFrameApplicationEvent;
import com.github.kancyframework.springx.swing.action.JFrameApplicationListener;
import com.github.kancyframework.springx.swing.dialog.InputDialog;
import com.github.kancyframework.springx.swing.filechooser.SimpleFileChooser;
import com.github.kancyframework.springx.utils.DateUtils;
import com.github.kancyframework.springx.utils.FileUtils;
import com.github.kancyframework.springx.utils.RandomUtils;
import com.kancy.tester.domain.BankCard;
import com.kancy.tester.domain.CardBin;
import com.kancy.tester.ui.BankCardPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * BatchGenBankCardNoActionListener
 *
 * @author huangchengkang
 * @date 2021/8/27 18:51
 */
@Action({"批量生成银行卡"})
@Component
public class BatchGenBankCardNoActionListener extends JFrameApplicationListener {
    @Autowired
    private BankCardPanel bankCardPanel;
    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(JFrameApplicationEvent event) {

        String lineChar = "\r\n";
        String splitChar = ",";

        StringBuilder sb = new StringBuilder();
        sb.append("序号,卡号,卡类型,卡名称,银行名称,银行简称,卡bin,卡长度").append(lineChar);

        InputDialog inputDialog = new InputDialog(bankCardPanel, "需要生成多少银行卡测试数据?") {
            @Override
            protected JComponent getInputComponent() {
                SpinnerModel model = new SpinnerNumberModel(1000, 0, 100000, 10);
                JSpinner spinner = new JSpinner(model);
                spinner.setPreferredSize(new Dimension(150, 30));
                return spinner;
            }
        };
        inputDialog.show();

        Object inputValue = inputDialog.getInputValue();
        if (Objects.isNull(inputValue)){
            return;
        }

        int maxSize = (Integer) inputValue;

        SimpleFileChooser dialog = new SimpleFileChooser(event.getSource(), "保存银行卡信息文件");
        dialog.setFileNameExtensionFilter("支持csv，txt格式", new String[]{"csv", "txt"});
        dialog.setSelectedFile(new File(String.format("测试数据_银行卡_%s_%s.csv", maxSize, DateUtils.getNowTimestampStr())));
        dialog.showSaveDialog(fileChooser -> {
            String selectedFilePath = fileChooser.getSelectedFilePath();
            try {
                // 生成数据
                for (int i = 1; i <= maxSize; i++) {

                    Object selectedItem = bankCardPanel.getBankCardTypeComboBox().getSelectedItem();
                    Object searchCardType = Objects.equals(selectedItem, "所有") ?
                            (RandomUtils.nextInt(10000) % 2 == 0 ? "储蓄卡" : "信用卡") : selectedItem;
                    Object searchBankName = bankCardPanel.getBankNameComboBox().getSelectedItem();

                    Object indexKey = null;
                    if (Objects.equals(searchBankName, "所有")){
                        indexKey = searchCardType;
                    }else {
                        indexKey = String.format("%s@%s", searchBankName, searchCardType);
                    }
                    BankCard bankCard = bankCardPanel.getBankCardService().generateCard(String.valueOf(indexKey));
                    CardBin cardBin = bankCard.getCardBin();
                    sb.append(i).append(splitChar)
                            .append(bankCard.getCardNo()).append("\t").append(splitChar)
                            .append(cardBin.getCardType()).append(splitChar)
                            .append(cardBin.getCardName()).append(splitChar)
                            .append(cardBin.getBankName()).append(splitChar)
                            .append(Objects.isNull(cardBin.getBankAbbr()) ? "":cardBin.getBankAbbr()).append(splitChar)
                            .append(cardBin.getId()).append(splitChar)
                            .append(cardBin.getCardLength()).append(lineChar);
                }
                // 写到文件
                FileUtils.writeByteArrayToFile(sb.toString().getBytes("GBK"), new File(selectedFilePath));
                Swing.msg(bankCardPanel, "银行卡数据生成成功！");
            } catch (IOException e) {
                Swing.msg(bankCardPanel, "银行卡数据生成失败！");
            }
        });

    }

}
