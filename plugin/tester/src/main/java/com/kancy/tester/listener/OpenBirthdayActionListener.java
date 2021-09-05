package com.kancy.tester.listener;

/**
 * OpenDistrictActionListener
 *
 * @author huangchengkang
 * @date 2021/8/28 14:02
 */

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.JFrameApplicationEvent;
import com.github.kancyframework.springx.swing.action.JFrameApplicationListener;
import com.github.kancyframework.springx.swing.action.KeyStroke;
import com.github.kancyframework.springx.utils.DateUtils;
import com.github.kancyframework.springx.utils.StringUtils;
import com.kancy.spring.minidb.MapDb;
import com.kancy.tester.ui.BirthdayDialog;
import com.kancy.tester.ui.IdCardPanel;
import com.kancy.tester.utils.IDCardUtils;

@Action({"设置出生日期"})
@KeyStroke("ctrl 4")
@Component
public class OpenBirthdayActionListener extends JFrameApplicationListener {

    @Autowired
    private IdCardPanel idCardPanel;

    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(JFrameApplicationEvent event) {
        BirthdayDialog dialog = new BirthdayDialog(event.getSource());
        dialog.setTitle("请选择出生日期");
        dialog.setLocationRelativeTo(idCardPanel);
        dialog.setDefaultDate(MapDb.getData("defaultBirthDate"));
        String idCardAgeRange = MapDb.getData("idCardAgeRange");
        if (StringUtils.isNotBlank(idCardAgeRange)){
            String[] array = StringUtils.toArray(idCardAgeRange, "-");
            int minAge = Integer.parseInt(array[0]);
            int maxAge = Integer.parseInt(array[1]);
            int currYear = DateUtils.getCurrYear();
            dialog.setYearRange(String.format("%s-%s", currYear - maxAge, currYear - minAge));
        }
        dialog.showDialog(date -> {
            IDCardUtils.setBirthday(date);
            MapDb.putData("defaultBirthDate", date);
            dialog.dispose();
            Swing.msg(idCardPanel, "设置成功！");
        });
    }
}
