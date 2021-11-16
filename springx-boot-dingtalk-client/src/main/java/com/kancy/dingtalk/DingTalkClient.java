package com.kancy.dingtalk;

import com.kancy.dingtalk.request.DingTalkRequest;

/**
 * DingTalkClient
 *
 * @author huangchengkang
 * @date 2021/11/16 11:47
 */
interface DingTalkClient {
    /**
     * 发送文本消息
     *
     * @param content 文本内容
     * @param atAll   @所有人
     */
    void sendText(String content, boolean atAll);

    /**
     * 发送文本消息
     *
     * @param content  文本内容
     * @param atMobile @某些人
     */
    void sendText(String content, String... atMobile);

    /**
     * 发Markdown消息
     *
     * @param title   标题
     * @param content 内容
     * @param atAll   @所有人
     */
    void sendMarkdown(String title, String content, boolean atAll);

    /**
     * 发Markdown消息
     *
     * @param title    标题
     * @param content  内容
     * @param atMobile @某些人
     */
    void sendMarkdown(String title, String content, String... atMobile);

    /**
     * 发送链接
     *
     * @param title      标题
     * @param text       文本
     * @param messageUrl 消息url
     * @param picUrl     图片url
     */
    void sendLink(String title, String text, String messageUrl, String picUrl);

    /**
     * 发送链接
     *
     * @param title      标题
     * @param text       文本
     * @param messageUrl 消息url
     * @param picUrl     图片url
     * @param atAll      @所有人
     */
    void sendLink(String title, String text, String messageUrl, String picUrl, boolean atAll);

    /**
     * 发送请求
     *
     * @param request 请求
     */
    void send(DingTalkRequest request);

    /**
     * 发送请求
     *
     * @param requestBody 请求体
     */
    void send(String requestBody);

}
