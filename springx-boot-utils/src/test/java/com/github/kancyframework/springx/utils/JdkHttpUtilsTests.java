package com.github.kancyframework.springx.utils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class JdkHttpUtilsTests {
    @Test
    public void test01() throws IOException {
        String httpUrl = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2407138699,2222400934&fm=26&gp=0.jpg";
        JdkHttpUtils.downloadFile(httpUrl,"yes.jpg");
        JdkHttpUtils.downloadFile(httpUrl,new File("yes.jpg"));
    }
}
