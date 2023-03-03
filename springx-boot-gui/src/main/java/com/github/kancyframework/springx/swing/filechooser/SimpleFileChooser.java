package com.github.kancyframework.springx.swing.filechooser;

import java.awt.*;
import java.io.File;

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
        setOnlyFileSelection();
    }

    public SimpleFileChooser(Component parentComponent, String title) {
        super(parentComponent, title);
        setOnlyFileSelection();
    }

    public AbstractFileChooser setSelectedFile(File file){
        fileChooser.setSelectedFile(file);
        return this;
    }

}
