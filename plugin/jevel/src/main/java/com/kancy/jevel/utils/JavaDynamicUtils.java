package com.kancy.jevel.utils;

import com.github.kancyframework.springx.utils.FileUtils;
import com.kancy.jevel.classloader.MemoryClassLoader;

import java.io.File;
import java.io.IOException;

/**
 * JavaDynamicUtils
 *
 * @author huangchengkang
 * @date 2021/9/18 1:34
 */
public class JavaDynamicUtils {

    public static Class<?> forClass(File file)
            throws ClassNotFoundException, IOException {
        String javaSrc = FileUtils.readFileToString(file);
        return forClass(javaSrc);
    }

    public static Class<?> forClass(String javaSrc)
            throws ClassNotFoundException {
        MemoryClassLoader loader = MemoryClassLoader.getInstance();
        String className = loader.getClassNameByJavaSrc(javaSrc);
        loader.registerJava(className, javaSrc);
        return loader.findClass(className);
    }
}
