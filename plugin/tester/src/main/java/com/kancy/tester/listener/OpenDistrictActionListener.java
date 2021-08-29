package com.kancy.tester.listener;

/**
 * OpenDistrictActionListener
 *
 * @author huangchengkang
 * @date 2021/8/28 14:02
 */

import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.JFrameApplicationEvent;
import com.github.kancyframework.springx.swing.action.JFrameApplicationListener;
import com.github.kancyframework.springx.swing.action.KeyStroke;
import com.github.kancyframework.springx.swing.utils.SystemUtils;
import com.kancy.tester.ui.DistrictDialog;

import java.awt.*;

@Action({"设置行政区划"})
@KeyStroke("ctrl 2")
@Component
public class OpenDistrictActionListener extends JFrameApplicationListener {

    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(JFrameApplicationEvent event) {
        DistrictDialog dialog = new DistrictDialog(event.getSource());
        dialog.setTitle("请选择行政区划");
        if (SystemUtils.isMacOS()){
            dialog.setSize(new Dimension(500, 100));
        }else {
            dialog.setSize(new Dimension(600, 120));
        }
        dialog.setResizable(false);
        dialog.setVisible(true);
    }
}
