package com.kancy.imagebeder.config;

import lombok.Data;

import java.io.Serializable;

/**
 * ImagebedConfig
 *
 * @author huangchengkang
 * @date 2022/1/19 0:34
 */
@Data
public class ImagebedConfig implements Serializable {
    private static final long serialVersionUID = 20220119003431173L;

    private String username;
    private String accessToken;
    private String repoName;
    private String basePath;
    private String remark;

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(username).append("@");
        if (basePath.startsWith("/")){
            sb.append(repoName).append(":/").append(basePath);
        }else {
            sb.append(repoName).append("://").append(basePath);
        }
        return sb.toString();
    }
}
