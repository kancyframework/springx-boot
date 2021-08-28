package com.github.kancyframework.springx.swing.utils;

import com.github.kancyframework.springx.utils.FileUtils;
import com.github.kancyframework.springx.utils.StringUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

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
        if (classResourcePath.startsWith("/")){
            return new ImageIcon(ImageUtils.class.getResource(classResourcePath));
        }
        return new ImageIcon(ImageUtils.class.getClassLoader().getResource(classResourcePath));
    }

    public static Icon createImageIcon(File file) throws MalformedURLException {
        return new ImageIcon(file.toURL());
    }

    public static Image createImage(String classResourcePath) {
        if (classResourcePath.startsWith("/")){
            return new ImageIcon(ImageUtils.class.getResource(classResourcePath)).getImage();
        }
        return new ImageIcon(ImageUtils.class.getClassLoader().getResource(classResourcePath)).getImage();
    }

    public static Image createImage(File file) throws MalformedURLException {
        ImageIcon icon = new ImageIcon(file.toURL());
        return icon.getImage();
    }

    public static byte[] getComponentImage(Component component) throws IOException {
        BufferedImage bi = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D  g2d = bi.createGraphics();
        component.paint(g2d);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bi, "PNG", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static boolean writeComponentImage(Component component, String filePath) throws IOException {
        BufferedImage bi = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D  g2d = bi.createGraphics();
        component.paint(g2d);
        File file = FileUtils.createNewFile(filePath);
        return ImageIO.write(bi, StringUtils.getFileExtName(filePath), file);
    }

}
