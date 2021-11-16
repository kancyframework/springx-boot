package com.kancy.dingtalk.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 丁磊谈话请求
 * DingTalkRequest
 *
 * @author huangchengkang
 * @date 2021/11/16 10:28
 */
public abstract class DingTalkRequest {

    private String msgtype;

    private At at;

    protected DingTalkRequest(String msgtype) {
        this.msgtype = msgtype;
        this.at = new At();
        this.at.setAtMobiles(new ArrayList<>());
        this.at.setAtUserIds(new ArrayList<>());
    }

    public void atAll(){
        this.at.setAtAll(true);
    }

    public void atMobile(String mobile){
        this.at.getAtMobiles().add(mobile);
        this.at.setAtAll(false);
    }

    public void atMobiles(String... mobiles){
        this.at.getAtMobiles().addAll(Arrays.asList(mobiles));
        this.at.setAtAll(false);
    }

    public void atUserId(String userId){
        this.at.getAtUserIds().add(userId);
        this.at.setAtAll(false);
    }

    public void atUserIds(String... userIds){
        this.at.getAtUserIds().addAll(Arrays.asList(userIds));
        this.at.setAtAll(false);
    }

    static class At {
        private boolean isAtAll;
        private List<String> atMobiles;
        private List<String> atUserIds;

        public boolean isAtAll() {
            return isAtAll;
        }

        public void setAtAll(boolean atAll) {
            isAtAll = atAll;
        }

        public List<String> getAtMobiles() {
            return atMobiles;
        }

        public void setAtMobiles(List<String> atMobiles) {
            this.atMobiles = atMobiles;
        }

        public List<String> getAtUserIds() {
            return atUserIds;
        }

        public void setAtUserIds(List<String> atUserIds) {
            this.atUserIds = atUserIds;
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("{");
            sb.append('"').append("isAtAll").append('"').append(":").append(isAtAll());
            appendList(sb, atMobiles, "atMobiles");
            appendList(sb, atUserIds, "atUserIds");
            sb.append("}");
            return sb.toString();
        }

        private void appendList(StringBuffer sb, List<String> list, String name) {
            if (!list.isEmpty()){
                StringBuffer atMobilesSb = new StringBuffer();
                atMobilesSb.append("[");
                for (String atMobile : list) {
                    atMobilesSb.append('"').append(atMobile).append('"').append(",");
                }
                atMobilesSb.deleteCharAt(atMobilesSb.length()-1);
                atMobilesSb.append("]");

                sb.append(",").append('"').append(name).append('"').append(":").append(atMobilesSb);
            } else {
                sb.append(",").append('"').append(name).append('"').append(":").append("[]");
            }
        }
    }

    public String getMsgtype() {
        return msgtype;
    }

    protected void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    protected At getAt() {
        return at;
    }

    protected void setAt(At at) {
        this.at = at;
    }

    protected boolean notEmpty(String str) {
        return Objects.nonNull(str) && !str.isEmpty();
    }

    protected String toStringBuilder(Object otStringObject){
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append('"').append("msgtype").append('"').append(":").append('"').append(getMsgtype()).append('"').append(",");
        sb.append('"').append(getMsgtype()).append('"').append(":").append(otStringObject).append(",");
        sb.append('"').append("at").append('"').append(":").append(getAt());
        sb.append("}");
        return sb.toString();
    }
}
