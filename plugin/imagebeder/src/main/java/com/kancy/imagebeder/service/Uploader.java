package com.kancy.imagebeder.service;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.utils.*;
import com.kancy.imagebeder.config.ImagebedConfig;
import com.kancy.imagebeder.config.Settings;
import net.coobird.thumbnailator.Thumbnails;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Uploader
 *
 * @author huangchengkang
 * @date 2022/1/19 10:06
 */
@Component
public class Uploader {

    @Autowired
    private Settings settings;

    public UploadResult upload(String configKey, byte[] bytes) throws IOException {
        return upload(configKey, "screenshot.jpg", bytes);
    }

    public UploadResult upload(String configKey, String filePath, byte[] bytes) throws IOException {
        ImagebedConfig imagebedConfig = settings.getConfigs().get(configKey);
        String accessToken = imagebedConfig.getAccessToken();
        String owner= imagebedConfig.getUsername();
        String repo = imagebedConfig.getRepoName();

        File file = new File(filePath);
        String fileExtName = StringUtils.getFileExtName(filePath);
        String fileSimpleName = StringUtils.getFileSimpleName(filePath);
        String uploadFileName = String.format("%s_%s.%s",  fileSimpleName, System.currentTimeMillis(), fileExtName);
        String path = String.format("/%s/%s/%s", imagebedConfig.getBasePath(), DateUtils.getNowPathStr(), uploadFileName);

        String uploadUrl = String.format("https://gitee.com/api/v5/repos/%s/%s/contents/%s", owner,repo,path);
        String content = Base64Utils.encodeBytes(getFileBytes(bytes));
        String message = String.format("上传文件：%s", uploadFileName);

        Map<String, Object> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("content", content);
        params.put("message", message);

        // 'Content-Type: application/json;charset=UTF-8'
        try {
            JdkHttpUtils.postJson(uploadUrl, SimpleJsonUtils.toJSONString(params));
            String url = "https://gitee.com/kancy666/images/raw/master" + path;

            UploadResult uploadResult = new UploadResult();
            uploadResult.setDownloadUrl(url);
            uploadResult.setFileName(file.getName());
            uploadResult.setUploadFileName(uploadFileName);

            if (settings.isLinkEnabled()){
                uploadResult.setHtmlText(String.format(
                        "<a href=\"%s\"><img src=\"%s\" alt=\"%s\" border=\"0\" /></a>", url, url, file.getName()));
                uploadResult.setMarkdownText(String.format("[![%s](%s)](%s)", file.getName(), url, url));
            }else {
                uploadResult.setHtmlText(String.format(
                        "<img src=\"%s\" alt=\"%s\" border=\"0\" />", url, file.getName()));
                uploadResult.setMarkdownText(String.format("![%s](%s)", file.getName(), url));
            }
            return uploadResult;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public UploadResult upload(String configKey, String filePath) throws IOException {
        ImagebedConfig imagebedConfig = settings.getConfigs().get(configKey);
        String accessToken = imagebedConfig.getAccessToken();
        String owner= imagebedConfig.getUsername();
        String repo = imagebedConfig.getRepoName();

        File file = new File(filePath);
        String fileExtName = StringUtils.getFileExtName(filePath);
        String fileSimpleName = StringUtils.getFileSimpleName(filePath);
        String uploadFileName = String.format("%s_%s.%s",  fileSimpleName, System.currentTimeMillis(), fileExtName);
        String path = String.format("/%s/%s/%s", imagebedConfig.getBasePath(), DateUtils.getNowPathStr(), uploadFileName);

        String uploadUrl = String.format("https://gitee.com/api/v5/repos/%s/%s/contents/%s", owner,repo, getURLEncoderString(path));
        String content = Base64Utils.encodeBytes(getFileBytes(file));
        String message = String.format("上传文件：%s", uploadFileName);

        Map<String, Object> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("content", content);
        params.put("message", message);

        // 'Content-Type: application/json;charset=UTF-8'
        try {
            String s = JdkHttpUtils.postJson(uploadUrl, SimpleJsonUtils.toJSONString(params));
            System.out.println(s);
            String url = String.format("https://gitee.com/%s/%s/raw/master%s" , owner, repo, getURLEncoderString(path));

            UploadResult uploadResult = new UploadResult();
            uploadResult.setDownloadUrl(url);
            uploadResult.setFileName(file.getName());
            uploadResult.setUploadFileName(uploadFileName);

            if (settings.isLinkEnabled()){
                uploadResult.setHtmlText(String.format(
                        "<a href=\"%s\"><img src=\"%s\" alt=\"%s\" border=\"0\" /></a>", url, url, file.getName()));
                uploadResult.setMarkdownText(String.format("[![%s](%s)](%s)", file.getName(), url, url));
            }else {
                uploadResult.setHtmlText(String.format(
                        "<img src=\"%s\" alt=\"%s\" border=\"0\" />", url, file.getName()));
                uploadResult.setMarkdownText(String.format("![%s](%s)", file.getName(), url));
            }
            return uploadResult;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getFileBytes(File file) throws IOException {
        double maxLength = 1024 * 1024;
        long fileLength = file.length();
        if (fileLength > maxLength){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            double rate = maxLength/fileLength;
            System.out.println(rate + " , " + Math.sqrt(rate));
            Thumbnails.of(file)
                    .outputQuality(1.0)
                    .scale((Math.sqrt(rate)+rate)/2.1)
                    .allowOverwrite(true)
                    .toOutputStream(byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            System.out.println(bytes.length/(1024.0*1024));
            return bytes;
        }
        return FileUtils.readFileToByteArray(file);
    }
    private byte[] getFileBytes(byte[] bytes) throws IOException {
        double maxLength = 1024 * 1024;
        long fileLength = bytes.length;
        if (fileLength > maxLength){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            double rate = maxLength/fileLength;
            System.out.println(rate + " , " + Math.sqrt(rate));
            Thumbnails.of(new ByteArrayInputStream(bytes))
                    .outputQuality(1.0)
                    .scale((Math.sqrt(rate)+rate)/2.1)
                    .allowOverwrite(true)
                    .toOutputStream(byteArrayOutputStream);
            byte[] newBytes = byteArrayOutputStream.toByteArray();
            System.out.println(bytes.length/(1024.0*1024));
            return newBytes;
        }
        return bytes;
    }

    private final static String ENCODE = "UTF-8";

    /**
     * URL 转码
     *
     * @return String
     * @author lifq
     * @date 2015-3-17 下午04:10:28
     */
    public static String getURLEncoderString(String path) {
        try {
            return URLEncoder.encode(path, ENCODE);
        } catch (UnsupportedEncodingException e) {
            return path;
        }
    }

}
