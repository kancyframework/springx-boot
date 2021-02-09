package com.github.kancyframework.springx.swing.utils;

import javax.swing.*;
import java.awt.*;

/**
 * ImageUtils
 *
 * @author kancy
 * @date 2021/1/9 19:14
 */
public class ImageUtils {

    private static final Icon questionMarkIcon = createImageIcon("images/question_mark_16x16.png");

    /**
     * 获取问号图片
     * @return
     */
    public static Icon getQuestionMarkIcon() {
        return questionMarkIcon;
    }

    public static Icon createImageIcon(String classResourcePath) {
        return new ImageIcon(ImageUtils.class.getClassLoader().getResource(classResourcePath));
    }

    public static Image createImage(String classResourcePath) {
        ImageIcon icon = new ImageIcon(ImageUtils.class.getClassLoader().getResource(classResourcePath));
        return icon.getImage();
    }

}
