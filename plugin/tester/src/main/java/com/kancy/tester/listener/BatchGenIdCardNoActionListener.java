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
import com.github.kancyframework.springx.utils.StringUtils;
import com.kancy.spring.minidb.MapDb;
import com.kancy.tester.ui.IdCardPanel;
import com.kancy.tester.utils.IDCardUtils;
import com.kancy.tester.utils.MockDataUtils;
import com.kancy.tester.utils.NameUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

/**
 * BatchGenIdCardNoActionListener
 *
 * @author huangchengkang
 * @date 2021/8/27 18:51
 */
@Action({"批量生成身份证"})
@Component
public class BatchGenIdCardNoActionListener extends JFrameApplicationListener {
    @Autowired
    private IdCardPanel idCardPanel;
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
        sb.append("序号,身份证号码,姓名,性别,年龄,生日,星座,生肖,户籍地址,签发机关,有效期限,手机号,邮箱").append(lineChar);

        boolean selectedRandom = idCardPanel.getRandomCheckBox().isSelected();
        String defaultIdCardValidDate = MapDb.getData("defaultIdCardValidDate");

        InputDialog inputDialog = new InputDialog(idCardPanel, "需要生成多少身份证测试数据?") {
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

        SimpleFileChooser dialog = new SimpleFileChooser(event.getSource(), "保存身份证信息文件");
        dialog.setFileNameExtensionFilter("支持csv，txt格式", new String[]{"csv", "txt"});
        dialog.setSelectedFile(new File(String.format("测试数据_身份证_%s_%s.csv", maxSize, DateUtils.getNowTimestampStr())));
        dialog.showSaveDialog(fileChooser -> {
            String selectedFilePath = fileChooser.getSelectedFilePath();
            try {
                // 生成数据
                for (int i = 1; i <= maxSize; i++) {
                    String idCardNo = IDCardUtils.create();

                    String cardValidDate = defaultIdCardValidDate;
                    if (selectedRandom){
                        cardValidDate = IDCardUtils.getCardValidDate(idCardNo);
                    }else{
                        if(StringUtils.isBlank(defaultIdCardValidDate)){
                            cardValidDate = IDCardUtils.getCardValidDate(idCardNo);
                        }
                    }

                    sb.append(i).append(splitChar)
                            .append(idCardNo).append("\t").append(splitChar)
                            .append(NameUtils.fullName()).append(splitChar)
                            .append(Integer.parseInt(idCardNo.substring(16,17)) % 2 == 0 ? "女" : "男").append(splitChar)
                            .append(new Date().getYear() + 1900 - Integer.parseInt(idCardNo.substring(6,10))).append(splitChar)
                            .append(String.format("%s年%s月%s日",
                                    Integer.parseInt(idCardNo.substring(6,10)),
                                    Integer.parseInt(idCardNo.substring(10,12)),
                                    Integer.parseInt(idCardNo.substring(12,14)))).append(splitChar)
                            .append(IDCardUtils.getConstellationByIdCard(idCardNo)).append(splitChar)
                            .append(IDCardUtils.getAnimalByIdCard(idCardNo)).append(splitChar)
                            .append(IDCardUtils.getAddress(idCardNo)
                                    .replace("<br/>","")
                                    .replace("<html>","")
                                    .replace("</html>","")).append(splitChar)
                            .append(String.format("%s公安局", IDCardUtils.getCityAndTown(idCardNo))).append(splitChar)
                            .append(cardValidDate).append(splitChar)
                            .append(MockDataUtils.mobile()).append("\t").append(splitChar)
                            .append(MockDataUtils.email()).append(lineChar);
                }

                // 写到文件
                FileUtils.writeByteArrayToFile(sb.toString().getBytes("GBK"), new File(selectedFilePath));
                Swing.msg(idCardPanel, "身份证数据生成成功！");
            } catch (IOException e) {
                Swing.msg(idCardPanel, "身份证数据生成失败！");
            }
        });

    }

}
