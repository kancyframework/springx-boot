package com.github.kancyframework.springx.utils;

import java.io.*;
import java.util.Map;
import java.util.Objects;

/**
 * Templater
 *
 * @author huangchengkang
 * @date 2021/12/12 17:08
 */
public class Templater {

    /**
     * 模板化
     *
     * @param templateContent   模板内容
     * @param templateVariables 模板变量
     * @return {@link String}
     */
    public static String format(String templateContent, Map<String, Object> templateVariables){
        return format(templateContent, templateVariables, null);
    }

    /**
     * 模板化
     *
     * @param templateContent   模板内容
     * @param templateVariables 模板变量
     * @param defValue          def值
     * @return {@link String}
     */
    public static String format(String templateContent, Map<String, Object> templateVariables, String defValue){
        if (Objects.isNull(templateContent) || templateVariables.isEmpty()){
            return templateContent;
        }
        for (Map.Entry<String, Object> e : templateVariables.entrySet()) {
            Object value = e.getValue();
            if (Objects.isNull(value)){
                value = defValue;
            }
            templateContent = templateContent.replace(String.format("${%s}", e.getKey()), String.valueOf(value));
        }
        return templateContent;
    }

    /**
     * 格式化文件
     *
     * @param filePath          文件路径
     * @param templateVariables 模板变量
     * @return {@link String}
     * @throws IOException IO异常
     */
    public static String formatFile(String filePath, Map<String, Object> templateVariables) throws IOException {
        return formatFile(new File(filePath), templateVariables);
    }

    /**
     * 模板化
     *
     * @param file              文件
     * @param templateVariables 模板变量
     * @return {@link String}
     * @throws IOException IO异常
     */
    public static String formatFile(File file, Map<String, Object> templateVariables) throws IOException {
        String inputStreamText = getInputStreamText(new FileInputStream(file));
        return format(inputStreamText, templateVariables);
    }

    /**
     * 模板化
     *
     * @param inputStream       输入流
     * @param templateVariables 模板变量
     * @return {@link String}
     * @throws IOException IO异常
     */
    public static String format(InputStream inputStream, Map<String, Object> templateVariables) throws IOException {
        String inputStreamText = getInputStreamText(inputStream);
        return format(inputStreamText, templateVariables);
    }

    /**
     * 模板化
     *
     * @param filePath          文件路径
     * @param templateVariables 模板变量
     * @param defValue          def值
     * @return {@link String}
     * @throws IOException IO异常
     */
    public static String formatFile(String filePath, Map<String, Object> templateVariables, String defValue) throws IOException {
        if (filePath.startsWith("classpath:")){
            String classFilePath = filePath.substring(10);
            if (!classFilePath.startsWith("/")){
                classFilePath = "/".concat(classFilePath);
            }
            return format(Templater.class.getResourceAsStream(classFilePath), templateVariables, defValue);
        }
        return formatFile(new File(filePath), templateVariables, defValue);
    }

    /**
     * 模板化
     *
     * @param file              文件
     * @param templateVariables 模板变量
     * @param defValue          def值
     * @return {@link String}
     * @throws IOException IO异常
     */
    public static String formatFile(File file, Map<String, Object> templateVariables, String defValue) throws IOException {
        String inputStreamText = getInputStreamText(new FileInputStream(file));
        return format(inputStreamText, templateVariables, defValue);
    }

    /**
     * 模板化
     *
     * @param inputStream       输入流
     * @param templateVariables 模板变量
     * @param defValue          def值
     * @return {@link String}
     * @throws IOException IO异常
     */
    public static String format(InputStream inputStream, Map<String, Object> templateVariables, String defValue) throws IOException {
        String inputStreamText = getInputStreamText(inputStream);
        return format(inputStreamText, templateVariables, defValue);
    }

    /**
     * 获取输入流文本
     *
     * @param inputStream 输入流
     * @return {@link String}
     * @throws IOException IO异常
     */
    private static String getInputStreamText(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            byte[] buf = new byte[1024];
            int read = 0;
            while ((read = inputStream.read(buf)) != -1){
                outputStream.write(buf, 0, read);
            }
            return new String(outputStream.toByteArray());
        } finally {
            inputStream.close();
            outputStream.close();
        }
    }

}
