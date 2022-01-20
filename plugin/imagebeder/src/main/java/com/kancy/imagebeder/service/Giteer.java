package com.kancy.imagebeder.service;

import com.github.kancyframework.springx.utils.JdkHttpUtils;
import com.github.kancyframework.springx.utils.SimpleJsonUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Giteer
 *
 * @author huangchengkang
 * @date 2022/1/20 23:55
 */
public class Giteer {

    public static void createProject(String token, String projectName) throws IOException {
        String url = "https://gitee.com/api/v5/user/repos";
        Map<String, Object> params = new HashMap<>();
        params.put("access_token",token);
        params.put("name",projectName);
        params.put("description","图床仓库 : " + projectName);
        params.put("auto_init","true");
        params.put("private","false");
        JdkHttpUtils.setCurrentConnectTimeout(3000);
        JdkHttpUtils.setCurrentReadTimeout(1000);
        String result = JdkHttpUtils.postJson(url, SimpleJsonUtils.toJSONString(params));
        System.out.println(result);
    }

    public static void createBranch(String owner, String repo, String branchName, String token) throws IOException {
        String url = String.format("https://gitee.com/api/v5/repos/%s/%s/branches", owner, repo);
        Map<String, Object> params = new HashMap<>();
        params.put("access_token",token);
        params.put("refs","master");
        params.put("branch_name",branchName);
        JdkHttpUtils.setCurrentConnectTimeout(3000);
        JdkHttpUtils.setCurrentReadTimeout(2000);
        String result = JdkHttpUtils.postJson(url, SimpleJsonUtils.toJSONString(params));
        System.out.println(result);
    }

}
