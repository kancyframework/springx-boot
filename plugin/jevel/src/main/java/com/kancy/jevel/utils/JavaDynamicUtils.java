package com.kancy.jevel.utils;

import com.github.kancyframework.springx.utils.ExceptionUtils;
import com.github.kancyframework.springx.utils.FileUtils;
import com.github.kancyframework.springx.utils.ReflectionUtils;
import com.kancy.jevel.classloader.MemoryClassLoader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JavaDynamicUtils
 *
 * @author huangchengkang
 * @date 2021/9/18 1:34
 */
public class JavaDynamicUtils {

    private static final Map<String, Class<?>> classes = new HashMap<>();

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

    public static Object invoke(String javaSrc, Map<String,Object> params){
        return invoke(javaSrc, "handle", params);
    }

    public static Object invoke(String javaSrc, String methodName, Object ... args){
        try {
            Class<?> aClass = forClass(javaSrc);
            Object instance = aClass.newInstance();
            return ReflectionUtils.invokeMethod(instance, methodName, args);
        } catch (Exception e) {
            throw ExceptionUtils.unchecked(e);
        }
    }

    public static Object invokeStatic(String javaSrc, String methodName, Object ... args) {
        try {
            Class<?> aClass = forClass(javaSrc);
            return ReflectionUtils.invokeStaticMethod(aClass.getName(), methodName, args);
        } catch (Exception e) {
            throw ExceptionUtils.unchecked(e);
        }
    }
}
