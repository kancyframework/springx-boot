package com.kancy.dingtalk.response;

import com.kancy.dingtalk.exception.DingTalkException;

import java.util.Objects;

/**
 * DingTalkResponse
 *
 * @author huangchengkang
 * @date 2021/11/16 16:07
 */
public class DingTalkResponse {

    public DingTalkResponse(String response) {
        // 解析response
        parseDingTalkResponse(response);
    }

    /**
     * 解析响应
     * 案例：{"errcode":0,"errmsg":"ok"}
     * @param response
     */
    private void parseDingTalkResponse(String response) {
        try {
            if (Objects.isNull(response) || response.isEmpty()){
                return;
            } else {
                response = response.trim();
            }

            if (!response.startsWith("{") || !response.endsWith("}")){
                return;
            }
            response = response.replace("{","").replace("}", "");

            String[] items = response.split(",");
            for (String item : items) {
                String[] kv = item.split("\":");
                String key = kv[0];
                if (Objects.equals(key, "\"errcode")){
                    String val = kv[1].replace("\"","");
                    setErrcode(val);
                }
                if (Objects.equals(key, "\"errmsg")){
                    String val = kv[1].replace("\"","");
                    setErrmsg(val);
                }
            }
            setOk(Objects.equals(getErrcode(), "0"));
        } catch (Exception e) {
            throw new DingTalkException(String.format("解析DingTalkResponse失败: %s", response), e);
        }
    }

    /**
     * 错误代码
     */
    private String errcode;
    /**
     * 错误描述
     */
    private String errmsg;
    /**
     * 错误描述
     */
    private boolean ok;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
}
