package com.github.kancyframework.springx.swing.filechooser;

import java.awt.*;

/**
 * SimpleDirectoryChooser
 *
 * @author kancy
 * @date 2020/2/16 16:51
 */
public class SimpleDirectoryChooser extends SimpleFileDirectoryChooser {

    public SimpleDirectoryChooser() {
        super();
        setOnlyDirectorySelection();
    }

    public SimpleDirectoryChooser(String title) {
        super(title);
    }

    public SimpleDirectoryChooser(Component parentComponent, String title) {
        super(parentComponent, title);
    }
}
