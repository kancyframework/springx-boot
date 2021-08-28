package com.kancy.tester.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.JFrameApplicationEvent;
import com.github.kancyframework.springx.swing.action.JFrameApplicationListener;
import com.github.kancyframework.springx.swing.dialog.JTextFieldInputDialog;
import com.github.kancyframework.springx.utils.StringUtils;
import com.kancy.tester.domain.District;
import com.kancy.tester.domain.Town;
import com.kancy.tester.ui.IdCardPanel;
import com.kancy.tester.utils.IDCardUtils;

import java.util.Date;
import java.util.Objects;

/**
 * SettingDefaultNationActionListener
 *
 * @author huangchengkang
 * @date 2021/8/27 18:51
 */
@Action({"验证身份证号码"})
@Component
public class VerifyIdCardNoActionListener extends JFrameApplicationListener {
    @Autowired
    private IdCardPanel idCardPanel;
    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(JFrameApplicationEvent event) {
        String idCardNo = Swing.getInput(JTextFieldInputDialog.class, null,"请输入需要验证的身份证号码");
        if (StringUtils.isBlank(idCardNo)){
            Swing.msg(idCardPanel, "请先输入身份证号码！");
            return;
        }

        if (IDCardUtils.isPersonId(idCardNo)){
            Town town = District.getInstance().getTowns().get(idCardNo.substring(0, 6));
            if (Objects.nonNull(town)){
                Swing.msg(idCardPanel, "<html>身份证号码[<font color=green>{}</font>]有效！<br/><br/>" +
                        "性别：{}<br/>" +
                        "年龄：{}<br/>" +
                        "星座：{}<br/>" +
                        "生肖：{}<br/>" +
                        "生日：{}年{}月{}日<br/>" +
                        "身份证户籍地：{}{}{}<br/>" +
                        "</html>",idCardNo.trim(),
                            Integer.parseInt(idCardNo.substring(16,17)) % 2 == 0 ? "女" : "男",
                            new Date().getYear() + 1900 - Integer.parseInt(idCardNo.substring(6,10)),
                            IDCardUtils.getConstellationByIdCard(idCardNo),
                            IDCardUtils.getAnimalByIdCard(idCardNo),
                            idCardNo.substring(6,10),Integer.parseInt(idCardNo.substring(10,12)),Integer.parseInt(idCardNo.substring(12,14)),
                            town.getProvince().getProvinceName(),town.getCity().getCityName(),town.getTownName()
                        );
            }else {
                Swing.msg(idCardPanel, "<html>身份证号码[<font color=green>{}</font>]有效！</html>", idCardNo.trim());
            }
        } else {
            Swing.msg(idCardPanel, "<html>身份证号码[<font color=red>{}</font>]无效！</html>", idCardNo.trim());
        }
    }


}
