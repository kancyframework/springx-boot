package com.github.kancyframework.springx.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtils {

    /**
     * 加载属性
     * @param classpath
     * @return
     * @throws IOException
     */
    public static Properties loadClasspathProperties(String classpath) throws IOException {
        Properties properties = new Properties();
        if (classpath.startsWith("/")){
            properties.load(PropertiesUtils.class.getResourceAsStream(classpath));
        }else {
            properties.load(PropertiesUtils.class.getClassLoader().getResourceAsStream(classpath));
        }
        return properties;
    }

    /**
     * 加载属性
     * @param filePath
     * @return
     * @throws IOException
     */
    public static Properties loadFileProperties(String filePath) throws IOException {
        Properties properties = new Properties();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(PathUtils.getFileAbsolutePath(filePath));
            properties.load(fileInputStream);
        } finally {
            IoUtils.closeResource(fileInputStream);
        }
        return properties;
    }
}
