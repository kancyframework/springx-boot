package com.github.kancyframework.springx.utils;

import org.fit.cssbox.demo.ImageRenderer;
import org.fit.cssbox.layout.Dimension;

import java.io.File;
import java.io.FileOutputStream;

public class HtmlUtils {

    public static void html2Image(String srcHtmlPath, String imagePath,
                                  float width, float height) throws Exception {
        srcHtmlPath = PathUtils.getFileAbsolutePath(srcHtmlPath);
        imagePath = PathUtils.getFileAbsolutePath(imagePath);
        ImageRenderer render = new ImageRenderer();
        render.setLoadImages(true, true);
        render.setWindowSize(new Dimension(width, height), true);
        String url = new File(srcHtmlPath).toURI().toString();
        FileOutputStream out = new FileOutputStream(imagePath);
        render.renderURL(url, out);
        IoUtils.closeResource(out);
    }
}
