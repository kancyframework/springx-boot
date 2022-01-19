package com.github.kancyframework.springx.utils;

import org.junit.Test;

import java.io.IOException;

/**
 * FileUtilsTests
 *
 * @author huangchengkang
 * @date 2021/12/22 13:35
 */
public class FileUtilsTests {

    @Test
    public void test() throws IOException {
        System.out.println(PathUtils.getFileAbsolutePath("classpath:classpathfile.txt"));
        System.out.println(FileUtils.existsClassPathFile("classpathfile.txt"));
        System.out.println(FileUtils.readClasspathFileLines("classpathfile.txt"));
        System.out.println(FileUtils.readClasspathFileToString("classpathfile.txt"));
    }
}
