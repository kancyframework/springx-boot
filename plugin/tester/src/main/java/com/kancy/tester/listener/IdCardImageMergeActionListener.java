package com.kancy.tester.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.JFrameApplicationEvent;
import com.github.kancyframework.springx.swing.action.JFrameApplicationListener;
import com.kancy.spring.minidb.MapDb;
import com.kancy.tester.ui.IdCardPanel;

/**
 * AboutActionListener
 *
 * @author huangchengkang
 * @date 2021/8/27 18:51
 */
@Action({"身份证影像合并"})
@Component
public class IdCardImageMergeActionListener extends JFrameApplicationListener {
    @Autowired
    private IdCardPanel idCardPanel;
    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(JFrameApplicationEvent event) {
        MapDb.putData("idCardImageMergeEnabled",
                idCardPanel.getIdCardImageMergeCheckBoxMenuItem().isSelected());
    }
}
