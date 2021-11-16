package com.kancy.dingtalk.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ActionCardDingTalkRequest
 *
 * @author huangchengkang
 * @date 2021/11/16 10:35
 */
public final class ActionCardDingTalkRequest extends DingTalkRequest {

    private ActionCard actionCard;

    public ActionCardDingTalkRequest() {
        super("actionCard");
        this.actionCard = new ActionCard();
        this.actionCard.setBtns(new ArrayList<>());
        this.actionCard.setHideAvatar(0);
        this.actionCard.setBtnOrientation(1);
    }

    public ActionCardDingTalkRequest(String title, String markdownText) {
        this();
        this.actionCard.setTitle(title);
        this.actionCard.setText(markdownText);
    }

    public void setTitle(String title) {
        this.actionCard.setTitle(title);
    }

    public void setText(String text) {
        this.actionCard.setText(text);
    }

    public void setMarkdownText(String markdownText) {
        this.actionCard.setText(markdownText);
    }

    public void setHideAvatar(Integer hideAvatar) {
        this.actionCard.setHideAvatar(hideAvatar);
    }

    public void setBtnOrientation(Integer btnOrientation) {
        this.actionCard.setBtnOrientation(btnOrientation);
    }

    public void addBtn(String actionName, String actionURL) {
        this.actionCard.btns.add(new Btn(actionName, actionURL));
    }

    public void addBtn(String actionName, String actionURL, Integer index) {
        this.actionCard.btns.add(index, new Btn(actionName, actionURL));
    }

    private static class ActionCard {
        private String title;
        private String text;
        private List<Btn> btns;
        private Integer hideAvatar;
        private Integer btnOrientation;

        public void setTitle(String title) {
            this.title = title;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setBtns(List<Btn> btns) {
            this.btns = btns;
        }

        public void setHideAvatar(Integer hideAvatar) {
            this.hideAvatar = hideAvatar;
        }

        public void setBtnOrientation(Integer btnOrientation) {
            this.btnOrientation = btnOrientation;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append('"').append("text").append('"').append(":").append('"').append(text).append('"');
            if (notEmpty(title)){
                sb.append(",").append('"').append("title").append('"').append(":").append('"').append(title).append('"');
            }
            if (Objects.nonNull(hideAvatar)){
                sb.append(",").append('"').append("hideAvatar").append('"').append(":").append('"').append(hideAvatar).append('"');
            }
            if (Objects.nonNull(btnOrientation)){
                sb.append(",").append('"').append("btnOrientation").append('"').append(":").append('"').append(btnOrientation).append('"');
            }
            
            StringBuilder btnSb = new StringBuilder();
            if (!btns.isEmpty()){
                btnSb.append("[");
                for (Btn btn : btns) {
                    btnSb.append(btn).append(",");
                }
                btnSb.deleteCharAt(btnSb.length()-1);
                btnSb.append("]");
            } else {
                btnSb.append("[]");
            }
            sb.append(",").append('"').append("btns").append('"').append(":").append(btnSb);
            sb.append("}");
            return sb.toString();
        }

        private boolean notEmpty(String str) {
            return Objects.nonNull(str) && !str.isEmpty();
        }
    }

    private static class Btn {
        private String title;
        private String actionURL;

        public Btn(String title, String actionURL) {
            this.title = title;
            this.actionURL = actionURL;
        }

        public String getTitle() {
            return title;
        }

        public String getActionURL() {
            return actionURL;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append('"').append("title").append('"').append(":").append('"').append(title).append('"').append(",");
            sb.append('"').append("actionURL").append('"').append(":").append('"').append(actionURL).append('"');
            sb.append("}");
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        return toStringBuilder(actionCard);
    }
}
