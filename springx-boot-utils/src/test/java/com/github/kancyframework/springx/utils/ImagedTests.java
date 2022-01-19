package com.github.kancyframework.springx.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ImageUutils
 *
 * @author huangchengkang
 * @date 2022/1/18 13:42
 */
public class ImagedTests {
    public static void main(String[] args) throws IOException {

        // https://gitee.com/api/v5/swagger#/deleteV5ReposOwnerRepoContentsPath

        String access_token= "2d6c78c1e13ff10b0f6a001f0b4a2cdf";
        String owner= "kancy666";
        String repo = "images";

        String path = "/upload/20220118/test6.jpg";

        String content = Base64Utils.encodeBytes(FileUtils.readClasspathFileToByteArray("/test.bmp"));
        String message = String.format("上传文件：%s", path);
        String url = String.format("https://gitee.com/api/v5/repos/%s/%s/contents/%s", owner,repo,path);

        Map<String, Object> params = new HashMap<>();
        params.put("access_token", access_token);
        params.put("content", content);
        params.put("message", message);


        // 'Content-Type: application/json;charset=UTF-8'
        try {
            String s = JdkHttpUtils.postJson(url, SimpleJsonUtils.toJSONString(params));
            System.out.println(s);
            System.out.println("https://gitee.com/kancy666/images/raw/master" + path);
            System.out.println(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
