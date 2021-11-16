package com.kancy.dingtalk.request;

/**
 * TextDingTalkRequest
 *
 * @author huangchengkang
 * @date 2021/11/16 10:35
 */
public final class TextDingTalkRequest extends DingTalkRequest{

    private Text text;

    public TextDingTalkRequest() {
        super("text");
        this.text = new Text();
    }

    public TextDingTalkRequest(String content) {
        this();
        this.text.setContent(content);
    }

    public void setContent(String content) {
        this.text.setContent(content);
    }

    static class Text {
        private String content;

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("{");
            sb.append('"').append("content").append('"').append(":").append('"').append(content).append('"');
            sb.append("}");
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        return toStringBuilder(text);
    }
}
