package com.kancy.jevel.utils;

import com.github.kancyframework.springx.utils.FileUtils;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * JavaDynamicUtilsTests
 *
 * @author huangchengkang
 * @date 2021/12/22 13:30
 */
public class JavaDynamicUtilsTests {
    @Test
    public void test1() throws Exception {
        String javaSrc = FileUtils.readClasspathFileToString("/javafile/HelloDynamicHandler.java");
        Class<?> aClass = JavaDynamicUtils.forClass(javaSrc);
        Object instance = aClass.newInstance();
        Method main = aClass.getMethod("handle", Map.class);
        Object invoke = main.invoke(instance, new HashMap<>());
        System.out.println(invoke);
    }

    @Test
    public void test2() throws Exception {
        File javaSrcFile = new File("src/test/resources/javafile/HelloDynamicHandler.java");
        Class<?> aClass = JavaDynamicUtils.forClass(javaSrcFile);
        Object instance = aClass.newInstance();
        Method main = aClass.getMethod("handle", Map.class);
        Object invoke = main.invoke(instance, new HashMap<>());
        System.out.println(invoke);
    }
}
