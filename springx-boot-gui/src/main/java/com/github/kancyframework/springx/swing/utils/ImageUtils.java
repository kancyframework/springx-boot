package com.github.kancyframework.springx.swing.utils;

import javax.swing.*;

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

    private static Icon createImageIcon(String classResourcePath) {
        return new ImageIcon(ImageUtils.class.getClassLoader().getResource(classResourcePath));
    }

}
