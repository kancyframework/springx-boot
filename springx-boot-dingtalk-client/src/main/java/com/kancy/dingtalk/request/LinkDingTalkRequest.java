package com.kancy.dingtalk.request;

import java.util.Objects;

/**
 * LinkDingTalkRequest
 *
 * @author huangchengkang
 * @date 2021/11/16 10:35
 */
public final class LinkDingTalkRequest extends DingTalkRequest{

    private Link link;

    public LinkDingTalkRequest() {
        super("link");
        this.link = new Link();
    }

    public LinkDingTalkRequest(String text, String messageUrl, String picUrl) {
        this("", text, messageUrl, picUrl);
    }

    public LinkDingTalkRequest(String title, String text, String messageUrl,String picUrl) {
        this();
        this.link.setTitle(title);
        this.link.setText(text);
        this.link.setMessageUrl(messageUrl);
        this.link.setPicUrl(picUrl);
    }

    public void setTitle(String title) {
        this.link.setTitle(title);
    }

    public void setText(String text) {
        this.link.setText(text);
    }

    public void setMessageUrl(String messageUrl) {
        this.link.setMessageUrl(messageUrl);
    }

    public void setPicUrl(String picUrl) {
        this.link.setPicUrl(picUrl);
    }

    static class Link {
        private String title;
        private String text;
        private String messageUrl;
        private String picUrl;

        public void setTitle(String title) {
            this.title = title;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setMessageUrl(String messageUrl) {
            this.messageUrl = messageUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("{");
            sb.append('"').append("text").append('"').append(":").append('"').append(text).append('"');
            if (notEmpty(title)){
                sb.append(",").append('"').append("title").append('"').append(":").append('"').append(title).append('"');
            }
            if (notEmpty(messageUrl)){
                sb.append(",").append('"').append("messageUrl").append('"').append(":").append('"').append(messageUrl).append('"');
            }
            if (notEmpty(picUrl)){
                sb.append(",").append('"').append("picUrl").append('"').append(":").append('"').append(picUrl).append('"');
            }
            sb.append("}");
            return sb.toString();
        }

        private boolean notEmpty(String str) {
            return Objects.nonNull(str) && !str.isEmpty();
        }
    }

    @Override
    public String toString() {
        return toStringBuilder(link);
    }
}
