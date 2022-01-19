package com.github.kancyframework.springx.utils;

import java.io.File;
import java.util.Objects;

/**
 * PathUtils
 *
 * @author kancy
 * @date 2021/1/8 21:24
 */
public class PathUtils {

    public static String path(String ... paths){
        StringBuilder sb = new StringBuilder();
        for (String path : paths) {
            if (StringUtils.isNotBlank(path)){
                sb.append(path).append("/");
            }
        }
        if (sb.length() > 0){
            sb.deleteCharAt(sb.length()-1);
        }
        return format(sb.toString());
    }

    public static String append(String path,String append){
        return format(String.format("%s/%s", path, append));
    }

    public static String format(File file){
        return format(file.getAbsolutePath());
    }

    public static String format(String filePath){
        if (Objects.isNull(filePath)){
            return filePath;
        }
        return filePath.replaceAll("\\\\+","/")
                .replaceAll("/+","/");
    }

    public static String getFileAbsolutePath(String filePath){
        String classPathPrefix = "classpath:";
        if (filePath.startsWith(classPathPrefix)){
            String classPath = filePath.replace(classPathPrefix, "");
            if (!classPath.startsWith("/") ){
                classPath = "/" + classPath;
            }
            filePath = path(PathUtils.class.getResource("/").getFile(), classPath);
        }
        return format(new File(filePath).getAbsolutePath());
    }

    public static String classPath(String classpath){
        return String.format("classpath:%s", classpath);
    }
}
