package com.kancy.tester.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.JFrameApplicationEvent;
import com.github.kancyframework.springx.swing.action.JFrameApplicationListener;
import com.github.kancyframework.springx.swing.action.KeyStroke;
import com.github.kancyframework.springx.swing.utils.ImageUtils;
import com.github.kancyframework.springx.swing.utils.SystemUtils;
import com.github.kancyframework.springx.utils.ReflectionUtils;
import com.github.kancyframework.springx.utils.StringUtils;
import com.kancy.tester.ui.IdCardPanel;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

/**
 * SettingDefaultNationActionListener
 *
 * @author huangchengkang
 * @date 2021/8/27 18:51
 */
@Action({"复制当前图片"})
@KeyStroke("alt 3")
@Component
public class CopyIdCardImageActionListener extends JFrameApplicationListener {
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

        try {

            CardLayout cardLayout = (CardLayout) idCardPanel.getIdCardImagePanel().getLayout();
            Integer currentCard = ReflectionUtils.getField("currentCard", cardLayout, int.class);
            if (Objects.equals(currentCard, 0)){
                // 正面
                byte[] imageBytes = ImageUtils.getComponentImage(idCardPanel.getIdCardFrontImagePanel());
                SystemUtils.setClipboardImage(imageBytes);
            }else {
                // 反面
                byte[] imageBytes = ImageUtils.getComponentImage(idCardPanel.getIdCardBackImagePanel());
                SystemUtils.setClipboardImage(imageBytes);
            }

        } catch (IOException e) {
            Swing.msg(idCardPanel, "复制失败！");
        }

    }
}
