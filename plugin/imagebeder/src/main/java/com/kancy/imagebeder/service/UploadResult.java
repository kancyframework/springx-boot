package com.kancy.imagebeder.service;

import lombok.Data;

/**
 * UploadResult
 *
 * @author huangchengkang
 * @date 2022/1/19 1:08
 */
@Data
public class UploadResult {
    private String downloadUrl;
    private String markdownText;
    private String htmlText;
    private String fileName;
    private String uploadFileName;
}
