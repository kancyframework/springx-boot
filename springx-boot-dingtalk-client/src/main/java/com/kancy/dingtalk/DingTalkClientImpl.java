package com.kancy.dingtalk;

import com.kancy.dingtalk.exception.DingTalkException;
import com.kancy.dingtalk.request.DingTalkRequest;
import com.kancy.dingtalk.request.LinkDingTalkRequest;
import com.kancy.dingtalk.request.MarkdownDingTalkRequest;
import com.kancy.dingtalk.request.TextDingTalkRequest;
import com.kancy.dingtalk.response.DingTalkResponse;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

/**
 * DingTalkClientImpl
 *
 * @author huangchengkang
 * @date 2021/11/16 11:47
 */
public class DingTalkClientImpl extends HttpClient implements DingTalkClient {
    private String accessToken;
    private String secretKey;

    public DingTalkClientImpl(String accessToken) {
        this.accessToken = accessToken;
    }

    public DingTalkClientImpl(String accessToken, String secretKey) {
        this.accessToken = accessToken;
        this.secretKey = secretKey;
    }

    /**
     * 发送文本消息
     *
     * @param content 文本内容
     * @param atAll   @所有人
     */
    @Override
    public void sendText(String content, boolean atAll) {
        TextDingTalkRequest textDingTalkRequest = new TextDingTalkRequest(content);
        if (atAll) {
            textDingTalkRequest.atAll();
        }
        send(textDingTalkRequest);
    }

    /**
     * 发送文本消息
     *
     * @param content  文本内容
     * @param atMobile @某些人
     */
    @Override
    public void sendText(String content, String... atMobile) {
        TextDingTalkRequest textDingTalkRequest = new TextDingTalkRequest(content);
        if (Objects.nonNull(atMobile)) {
            textDingTalkRequest.atMobiles(atMobile);
        }
        send(textDingTalkRequest);
    }

    /**
     * 发Markdown消息
     *
     * @param title   标题
     * @param content 内容
     * @param atAll   @所有人
     */
    @Override
    public void sendMarkdown(String title, String content, boolean atAll) {
        if (Objects.isNull(title) || title.isEmpty()){
            throw new DingTalkException("markdown param title is empty.");
        }
        MarkdownDingTalkRequest textDingTalkRequest = new MarkdownDingTalkRequest(title, content);
        if (atAll) {
            textDingTalkRequest.atAll();
        }
        send(textDingTalkRequest);
    }

    /**
     * 发Markdown消息
     *
     * @param title    标题
     * @param content  内容
     * @param atMobile @某些人
     */
    @Override
    public void sendMarkdown(String title, String content, String... atMobile) {
        if (Objects.isNull(title) || title.isEmpty()){
            throw new DingTalkException("markdown param title is empty.");
        }
        MarkdownDingTalkRequest textDingTalkRequest = new MarkdownDingTalkRequest(title, content);
        if (Objects.nonNull(atMobile)) {
            textDingTalkRequest.atMobiles(atMobile);
        }
        send(textDingTalkRequest);
    }

    /**
     * 发送链接
     *
     * @param title      标题
     * @param text       文本
     * @param messageUrl 消息url
     * @param picUrl     图片url
     */
    @Override
    public void sendLink(String title, String text, String messageUrl, String picUrl) {
        send(new LinkDingTalkRequest(title, text, messageUrl, picUrl));
    }

    /**
     * 发送链接
     *
     * @param title      标题
     * @param text       文本
     * @param messageUrl 消息url
     * @param picUrl     图片url
     * @param atAll      @所有人
     */
    @Override
    public void sendLink(String title, String text, String messageUrl, String picUrl, boolean atAll) {
        LinkDingTalkRequest textDingTalkRequest = new LinkDingTalkRequest(title, text, messageUrl, picUrl);
        if (atAll) {
            textDingTalkRequest.atAll();
        }
        send(textDingTalkRequest);
    }

    /**
     * 发送请求
     *
     * @param request 请求
     */
    @Override
    public void send(DingTalkRequest request) {
        if (Objects.isNull(request)){
            throw new DingTalkException("dingtalk request is empty.");
        }
        send(request.toString());
    }

    /**
     * 发送请求
     *
     * @param requestBody 请求体
     */
    @Override
    public void send(String requestBody) {
        if (Objects.isNull(requestBody) || requestBody.isEmpty()){
            throw new DingTalkException("dingtalk request body is empty.");
        }

        String dingTalkServiceUrl = getDingTalkServiceUrl(accessToken, secretKey);
        try {
            String responseBody = postJson(dingTalkServiceUrl, requestBody);
            DingTalkResponse response = new DingTalkResponse(responseBody);
            if (!response.isOk()){
                throw new DingTalkException(response.getErrmsg());
            }
        } catch (IOException e) {
            throw new DingTalkException("http client error", e);
        }
    }

    /**
     * 获取ding talk服务url
     * https://developers.dingtalk.com/document/robots/customize-robot-security-settings
     *
     * @param accessToken 访问令牌
     * @param secret      秘密
     * @return {@link String}
     */
    protected String getDingTalkServiceUrl(String accessToken, String secret) {
        try {
            String url = String.format("https://oapi.dingtalk.com/robot/send?access_token=%s", accessToken);
            if (Objects.nonNull(secret) && !secret.isEmpty()) {
                Long timestamp = System.currentTimeMillis();
                String stringToSign = timestamp + "\n" + secret;
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
                String sign = URLEncoder.encode(new String(Base64.getEncoder().encode(signData)), StandardCharsets.UTF_8.name());
                url = String.format("%s&sign=%s&timestamp=%s", url, sign, timestamp);
            }
            return url;
        } catch (Exception e) {
            throw new DingTalkException("获取签名失败", e);
        }
    }

    /**
     * 获取访问令牌
     *
     * @return {@link String}
     */
    protected final String getAccessToken() {
        return accessToken;
    }

    /**
     * 获取密钥
     *
     * @return {@link String}
     */
    protected final String getSecretKey() {
        return secretKey;
    }
}
