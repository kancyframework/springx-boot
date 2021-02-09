package com.github.kancyframework.springx.utils;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

@Ignore
public class JdkHttpUtilsTests {
    private String httpUrl = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2407138699,2222400934&fm=26&gp=0.jpg";

    @Test
    public void test01() throws IOException {
        JdkHttpUtils.downloadFile(httpUrl,"yes.jpg");
    }

    @Test
    public void test02() throws IOException {
        JdkHttpUtils.downloadFile(httpUrl,new File("yes.jpg"));
    }

    @Test
    public void test03() throws IOException {
        JdkHttpUtils.download(httpUrl, "yes.jpg");
    }

    @Test
    public void test04() throws IOException {
        JdkHttpUtils.download(httpUrl, "yes.jpg", true);
    }

    @Test
    public void test05() throws IOException {
        String httpUrl = "https://gitee.com/kancy666/public/raw/master/jars/fastjson-1.2.69.jar";
        JdkHttpUtils.download(httpUrl, "fastjson.jar", false);
    }

}
