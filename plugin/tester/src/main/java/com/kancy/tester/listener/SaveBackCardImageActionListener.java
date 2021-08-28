package com.kancy.tester.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.JFrameApplicationEvent;
import com.github.kancyframework.springx.swing.action.JFrameApplicationListener;
import com.github.kancyframework.springx.swing.action.KeyStroke;
import com.github.kancyframework.springx.swing.filechooser.SimpleFileChooser;
import com.github.kancyframework.springx.swing.utils.ImageUtils;
import com.github.kancyframework.springx.utils.StringUtils;
import com.kancy.tester.ui.BankCardPanel;

import java.io.File;
import java.io.IOException;

/**
 * SettingDefaultNationActionListener
 *
 * @author huangchengkang
 * @date 2021/8/27 18:51
 */
@Action({"银行卡另存为"})
@KeyStroke("alt 3")
@Component
public class SaveBackCardImageActionListener extends JFrameApplicationListener {
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
            Swing.msg(event.getSource(), "银行卡信息为空，请先生成！");
            return;
        }
        SimpleFileChooser dialog = new SimpleFileChooser(event.getSource(), "银行卡图片另存为");
        dialog.setFileNameExtensionFilter("支持png，jpg格式", new String[]{"png", "jpg"});
        dialog.setSelectedFile(new File(String.format("银行卡_%s_%s.png",
                bankCardPanel.getBankNameLabel().getText().replace(" ", ""),
                bankCardNo.replace(" ", "")
                )));
        dialog.showSaveDialog(fileChooser -> {
            String selectedFilePath = fileChooser.getSelectedFilePath();
            try {
                ImageUtils.writeComponentImage(bankCardPanel.getBankCardImagePanel(), selectedFilePath);
                Swing.msg(bankCardPanel, "银行卡影像保存成功！");
            } catch (IOException e) {
                Swing.msg(bankCardPanel, "银行卡影像保存失败！");
            }
        });
    }
}
