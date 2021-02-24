package com.github.kancyframework.springx.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Md5Utils
 *
 * @author kancy
 * @date 2020/6/14 10:28
 */
public class Md5Utils {

    public static String md5(String buffer) {
        String string = null;
        char hexDigist[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(buffer.getBytes());
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
