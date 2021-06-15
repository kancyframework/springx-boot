package com.kancy.imageconvertor.listener;

import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.action.AbstractActionApplicationListener;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.ActionApplicationEvent;
import com.github.kancyframework.springx.swing.action.KeyStroke;
import com.github.kancyframework.springx.utils.PathUtils;
import com.kancy.imageconvertor.ui.ImageConvertor;
import net.sf.image4j.use.Image4j;

import javax.swing.*;
import java.io.File;

@Action({"转换"})
@KeyStroke("ctrl 1")
@Component
public class ConvertActionListener extends AbstractActionApplicationListener<ActionApplicationEvent<ImageConvertor>> {

    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ActionApplicationEvent<ImageConvertor> event) {
        JLabel label = event.getSource().getLabel();
        String type = (String) event.getSource().getComboBox().getSelectedItem();
        String strInputFilePath = (String) label.getClientProperty("path");
        String strOutputFilePath = strInputFilePath.substring(0, strInputFilePath.lastIndexOf(".")) + "." + type;
        boolean convert = Image4j.Convert(PathUtils.getFileAbsolutePath(strInputFilePath),
                PathUtils.getFileAbsolutePath(strOutputFilePath));
        Swing.msg(event.getSource(), "转换结果: {}", convert);

    }
}
