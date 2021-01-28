package com.github.kancyframework.springx.swing.filechooser;

import java.awt.*;

/**
 * SimpleFileDirectoryChooser
 *
 * @author kancy
 * @date 2020/2/16 16:51
 */
public class SimpleFileDirectoryChooser extends AbstractFileChooser {

    private String title = "\u6d4f\u89c8\u6587\u4ef6";

    public SimpleFileDirectoryChooser() {
        super();
        setFileSelection();
    }

    public SimpleFileDirectoryChooser(String title) {
        this();
        this.title = title;
    }

    public SimpleFileDirectoryChooser(Component parentComponent, String title) {
        super(parentComponent);
        this.title = title;
    }
}
