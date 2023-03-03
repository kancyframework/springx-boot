package com.github.kancyframework.springx.utils;

import java.nio.charset.StandardCharsets;

/**
 * NumberUtils
 *
 * @author huangchengkang
 * @date 2023/3/3 15:33
 */
public abstract class NumberUtils {

    private static final byte[] DIGITS = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90};

    /**
     * 10进制数字转换成62进制
     * @param num
     * @return
     */
    public static String to62Str(long num) {
        int radix = DIGITS.length;
        byte[] buf = new byte[65];
        int charPos = 64;

        for(num = -num; num <= (long)(-radix); num /= (long)radix) {
            buf[charPos--] = DIGITS[(int)(-(num % (long)radix))];
        }

        buf[charPos] = DIGITS[(int)(-num)];
        return new String(buf, charPos, 65 - charPos, StandardCharsets.UTF_8);
    }

    /**
     * 62进制转10进制
     *
     * @param s
     * @return
     */
    public static long parse62Str(String s) {
        long n = 0;
        long p = 1;
        int c = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            c = s.charAt(i);
            if (c > 96) {
                n += (c - 61) * p;// a-z转为数字
            } else if (c > 64) {
                n += (c - 55) * p;// A-Z转为数字
            } else {
                n += (c - 48) * p;// 0-9转为数字
            }
            p *= 62;
        }
        return n;
    }

}
