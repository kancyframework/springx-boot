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

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;

/**
 * SettingDefaultGirlHeadPhotoActionListener
 *
 * @author huangchengkang
 * @date 2021/8/27 18:51
 */
@Action({"设置默认女头像"})
@Component
public class SettingDefaultGirlHeadPhotoActionListener extends JFrameApplicationListener {
    @Autowired
    private IdCardPanel idCardPanel;
    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(JFrameApplicationEvent event) {
        String filePath = Swing.getInput(JTextFieldInputDialog.class, null,"设置默认女头像",
                MapDb.getData("defaultGirlHeadPhoto", "classpath:/images/id_card_front_photo_200x200_girl_3.png"));
        if (StringUtils.isBlank(filePath)){
            return;
        }

        String classPathPrefix = "classpath:";
        if (filePath.startsWith(classPathPrefix)){
            String classPath = filePath.replace(classPathPrefix, "");
            if (!classPath.startsWith("/") ){
                classPath = "/" + classPath;
            }
            idCardPanel.getImageHeadPhotoLabel().setIcon(new ImageIcon(getClass().getResource(classPath)));
        } else {
            try {
                idCardPanel.getImageHeadPhotoLabel().setIcon(new ImageIcon(new File(filePath).toURL()));
            } catch (MalformedURLException e) {
                Swing.msg(idCardPanel, "设置失败！");
                return;
            }
        }
        MapDb.putData("defaultGirlHeadPhoto", filePath);
        Swing.msg(idCardPanel, "设置成功！");
    }
}
