package com.kancy.dingtalk.request;

import java.util.Objects;

/**
 * MarkdownDingTalkRequest
 *
 * @author huangchengkang
 * @date 2021/11/16 10:35
 */
public final class MarkdownDingTalkRequest extends DingTalkRequest{

    private Markdown markdown;

    public MarkdownDingTalkRequest() {
        super("markdown");
        this.markdown = new Markdown();
    }

    public MarkdownDingTalkRequest(String title, String markdownContent) {
        this();
        this.markdown.setTitle(title);
        this.markdown.setText(markdownContent);
    }

    public void setTitle(String title) {
        this.markdown.setTitle(title);
    }

    public void setText(String text) {
        this.markdown.setText(text);
    }

    static class Markdown {
        private String title;
        private String text;

        public void setTitle(String title) {
            this.title = title;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("{");
            sb.append('"').append("title").append('"').append(":").append('"').append(title).append('"').append(",");
            sb.append('"').append("text").append('"').append(":").append('"').append(text).append('"');
            sb.append("}");
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        return toStringBuilder(markdown);
    }

}
