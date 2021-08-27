package com.kancy.tester.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.JFrameApplicationEvent;
import com.github.kancyframework.springx.swing.action.JFrameApplicationListener;
import com.github.kancyframework.springx.swing.action.KeyStroke;
import com.github.kancyframework.springx.swing.filechooser.SimpleDirectoryChooser;
import com.github.kancyframework.springx.swing.filechooser.SimpleFileChooser;
import com.github.kancyframework.springx.swing.filechooser.SimpleFileDialog;
import com.github.kancyframework.springx.swing.utils.ImageUtils;
import com.github.kancyframework.springx.utils.PathUtils;
import com.github.kancyframework.springx.utils.StringUtils;
import com.kancy.tester.ui.IdCardPanel;

import java.io.IOException;

/**
 * SettingDefaultNationActionListener
 *
 * @author huangchengkang
 * @date 2021/8/27 18:51
 */
@Action({"身份证另存为"})
@KeyStroke("alt 3")
@Component
public class SaveIdCardImageActionListener extends JFrameApplicationListener {
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
            Swing.msg(event.getSource(), "身份证信息为空，请先生成！");
            return;
        }
        SimpleDirectoryChooser dialog = new SimpleDirectoryChooser(event.getSource(), "身份证图片另存为");
        dialog.setOnlyDirectorySelection();
        dialog.showSaveDialog(fileChooser -> {
            String selectedFilePath = fileChooser.getSelectedFilePath();
            try {
                ImageUtils.writeComponentImage(idCardPanel.getIdCardFrontImagePanel(),
                        PathUtils.path(selectedFilePath, String.format("身份证正面_%s_%s.png",
                                idCardPanel.getNameTextField().getText(),
                                idCardPanel.getIdCardNoTextField().getText().replace(" ",""))));
                ImageUtils.writeComponentImage(idCardPanel.getIdCardBackImagePanel(),
                        PathUtils.path(selectedFilePath, String.format("身份证反面_%s_%s.png",
                                idCardPanel.getNameTextField().getText(),
                                idCardPanel.getIdCardNoTextField().getText().replace(" ",""))));
                Swing.msg(idCardPanel, "保存成功！");
            } catch (IOException e) {
                Swing.msg(idCardPanel, "保存失败！");
            }
        });
    }
}
