package com.github.kancyframework.springx.swing.filechooser;


import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Locale;

/**
 * FileNameExtensionFilter
 *
 * @author kancy
 * @date 2020/2/16 18:21
 */
public final class FileNameExtensionFilter extends FileFilter implements FilenameFilter {
    private String[] extensions;

    private javax.swing.filechooser.FileNameExtensionFilter fileNameExtensionFilter;

    public FileNameExtensionFilter(String ... extensions) {
        this(Arrays.toString(extensions), extensions);
    }

    public FileNameExtensionFilter(String description, String ... extensions) {
        this.extensions = extensions;
        fileNameExtensionFilter = new javax.swing.filechooser.FileNameExtensionFilter(description, extensions);
    }

    @Override
    public boolean accept(File dir, String name) {
        File file = new File(dir, name);
        if (file.isDirectory()){
            return true;
        }
        String fileName = name;
        System.out.println(fileName);
        int i = fileName.lastIndexOf('.');
        if (i > 0 && i < fileName.length() - 1) {
            String desiredExtension = fileName.substring(i+1).toLowerCase(Locale.ENGLISH);
            for (String extension : extensions) {
                if (desiredExtension.equals(extension.toLowerCase(Locale.ENGLISH))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean accept(File pathname) {
        return fileNameExtensionFilter.accept(pathname);
    }

    @Override
    public String getDescription() {
        return fileNameExtensionFilter.getDescription();
    }
}
