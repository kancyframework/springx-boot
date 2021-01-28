package com.github.kancyframework.springx.swing.filechooser;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FilenameFilter;

/**
 * RegexFilter
 *
 * @author kancy
 * @date 2020/2/16 16:32
 */
public class RegexFilter extends FileFilter implements FilenameFilter {

    private String regex = ".*\\..*" ;
    private String description;

    public RegexFilter(String regex) {
        this.regex = regex;
        this.description = String.format("\u6ee1\u8db3\u6b63\u5219：%s", regex);
    }

    public RegexFilter(String regex, String description) {
        this.regex = regex;
        this.description = description;
    }

    @Override
    public boolean accept(File f) {
        return f.getName().matches(regex);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.matches(regex);
    }
}
