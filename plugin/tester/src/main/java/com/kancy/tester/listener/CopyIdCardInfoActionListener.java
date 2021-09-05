package com.kancy.tester.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.log.Log;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.JFrameApplicationEvent;
import com.github.kancyframework.springx.swing.action.JFrameApplicationListener;
import com.github.kancyframework.springx.swing.action.KeyStroke;
import com.github.kancyframework.springx.swing.utils.SystemUtils;
import com.github.kancyframework.springx.utils.CalendarUtils;
import com.github.kancyframework.springx.utils.StringUtils;
import com.kancy.tester.ui.IdCardPanel;
import com.kancy.tester.utils.IDCardUtils;

import java.util.Date;

/**
 * SettingDefaultNationActionListener
 *
 * @author huangchengkang
 * @date 2021/8/27 18:51
 */
@Action({"复制身份证信息"})
@KeyStroke("alt 2")
@Component
public class CopyIdCardInfoActionListener extends JFrameApplicationListener {
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

        String solarConstellation = null;
        try {
            String solarDate = CalendarUtils.lunarToSolar(idCardNo.substring(6, 14));
            solarConstellation = IDCardUtils.getConstellation(
                    Integer.parseInt(solarDate.substring(4, 6)), Integer.parseInt(solarDate.substring(6, 8))
            );
        } catch (Exception e) {
        }

        StringBuilder sb = new StringBuilder();
        sb.append("姓名：").append(idCardPanel.getNameTextField().getText()).append("\r\n");
        sb.append("性别：").append(idCardPanel.getImageSexLabel().getText()).append("\r\n");
        sb.append("名族：").append(idCardPanel.getImageNationLabel().getText()).append("\r\n");
        sb.append("年龄：").append(new Date().getYear() + 1900 - Integer.parseInt(idCardNo.substring(6,10))).append("\r\n");

        if (StringUtils.isBlank(solarConstellation)){
            sb.append("星座：").append(IDCardUtils.getConstellationByIdCard(idCardNo)).append("\r\n");
        }else {
            sb.append("星座：")
                    .append(IDCardUtils.getConstellationByIdCard(idCardNo)).append("（阳历）")
                    .append("、")
                    .append(solarConstellation).append("（农历）").append("\r\n");
        }
        sb.append("生肖：").append(IDCardUtils.getAnimalByIdCard(idCardNo)).append("\r\n");
        sb.append("身份证号码：").append(idCardNo).append("\r\n");
        sb.append("出生生日：").append(String.format("%s年%s月%s日",
                Integer.parseInt(idCardNo.substring(6,10)),
                Integer.parseInt(idCardNo.substring(10,12)),
                Integer.parseInt(idCardNo.substring(12,14))
                )).append("\r\n");
        sb.append("户籍地址：").append(idCardPanel.getImageAddressLabel().getText()
                .replace("<html>","")
                .replace("</html>","")
                .replace("<br/>","")).append("\r\n");
        sb.append("签发机关：").append(idCardPanel.getImageQfjgLabel().getText()).append("\r\n");
        sb.append("有效期限：").append(idCardPanel.getImageCardValidDateLabel().getText()).append("\r\n");
        SystemUtils.setClipboardText(sb.toString());
        Swing.msg(idCardPanel, "复制成功！");
    }
}
