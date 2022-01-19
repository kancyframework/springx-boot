package com.kancy.imagebeder.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.action.AbstractActionApplicationListener;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.ActionApplicationEvent;
import com.github.kancyframework.springx.swing.utils.ImageUtils;
import com.github.kancyframework.springx.swing.utils.SystemUtils;
import com.kancy.imagebeder.config.Settings;
import com.kancy.imagebeder.ui.Imagebeder;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * ConfigSourceListener
 *
 * @author huangchengkang
 * @date 2022/1/18 23:36
 */
@Action({"从剪切板导入"})
@Component
public class ImportClipboardImageListener extends AbstractActionApplicationListener<ActionApplicationEvent<Imagebeder>> {

    @Autowired
    private Settings settings;
    @Autowired
    private Imagebeder imagebeder;

    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ActionApplicationEvent<Imagebeder> event) {
        Image imageFromClipboard = null;
        try {
            imageFromClipboard = SystemUtils.getImageFromClipboard();
            if (Objects.nonNull(imageFromClipboard)){
                Image newImg = imageFromClipboard.getScaledInstance(855, 450, Image.SCALE_SMOOTH);
                ImageIcon imageIcon = new ImageIcon(newImg);
                imagebeder.getLabel_img().setIcon(imageIcon);
                imagebeder.getLabel_img().putClientProperty("path", "bytes");
                imagebeder.getLabel_img().putClientProperty("bytes", ImageUtils.getImageBytes(imageFromClipboard));
            }else {
                Swing.msg(imagebeder, "剪切板上没有图片数据！");
            }
        } catch (Exception exception) {
            Swing.msg(imagebeder, "剪切板上没有图片数据！");
        }
    }
}
