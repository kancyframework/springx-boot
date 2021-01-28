package com.github.kancyframework.springx.swing.filechooser;

import java.awt.*;

/**
 * SimpleFileChooser
 *
 * @author kancy
 * @date 2020/2/16 16:03
 */
public class SimpleFileChooser extends SimpleFileDirectoryChooser {

    public SimpleFileChooser() {
        super();
        setOnlyFileSelection();
    }

    public SimpleFileChooser(String title) {
        super(title);
    }

    public SimpleFileChooser(Component parentComponent, String title) {
        super(parentComponent, title);
    }

}
