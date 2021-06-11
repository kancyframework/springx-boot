package com.github.kancyframework.springx.utils;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Md5Utils
 *
 * @author kancy
 * @date 2020/6/14 10:28
 */
public class Md5Utils {

    public static String fileMd5(File file, boolean upperCase) throws IOException {
        String fileMd5 = fileMd5(file);
        if (upperCase && StringUtils.isNotBlank(fileMd5)){
            return fileMd5.toUpperCase();
        }
        return fileMd5;
    }

    public static String fileMd5(File file) throws IOException {
        return md5(FileUtils.readFileToByteArray(file));
    }

    public static String md5(String buffer, boolean upperCase) {
        String md5 = md5(buffer.getBytes());
        if (upperCase && StringUtils.isNotBlank(md5)){
            return md5.toUpperCase();
        }
        return md5;
    }

    public static String md5(String buffer) {
        return md5(buffer.getBytes());
    }

    public static String md5(byte[] bufferBytes) {
        String string = null;
        char hexDigist[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");

            md.update(bufferBytes);
            //16个字节的长整数
            byte[] datas = md.digest();

            char[] str = new char[2 * 16];
            int k = 0;

            for (int i = 0; i < 16; i++) {
                byte b = datas[i];
                //高4位
                str[k++] = hexDigist[b >>> 4 & 0xf];
                //低4位
                str[k++] = hexDigist[b & 0xf];
            }
            string = new String(str);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return string;
    }
}
