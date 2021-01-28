package com.github.kancyframework.springx.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * StringUtils
 *
 * @author kancy
 * @date 2020/2/16 5:56
 */
public abstract class StringUtils {

    private static final String COMMA = ",";
    private static final String EMPTY_STRING = "";
    private static final String[] EMPTY_STRING_ARRAY = {};
    private static final String SPLIT_CHAR = COMMA;
    private static final String JOIN_CHAR = COMMA;

    /**
     * 空字符串
     * @return
     */
    public static String empty() {
        return EMPTY_STRING;
    }

    /**
     * 空字符串数组
     * @return
     */
    public static String[] emptyArray() {
        return EMPTY_STRING_ARRAY;
    }
    /**
     * 空字符串
     * @return
     */
    public static String comma() {
        return COMMA;
    }

    /**
     * 是否为空
     * @param object
     * @return
     */
    public static boolean isEmpty(String object){
        return Objects.isNull(object) || object.isEmpty();
    }

    /**
     * 是否不为空
     * @param object
     * @return
     */
    public static boolean isNotEmpty(String object){
        return !isEmpty(object);
    }

    /**
     * 是否空白
     * @param object
     * @return
     */
    public static boolean isBlank(String object){
        return Objects.isNull(object) || object.trim().isEmpty();
    }

    /**
     * 是否不是空白
     * @param object
     * @return
     */
    public static boolean isNotBlank(String object){
        return !isBlank(object);
    }

    /**
     * 添加字符串到字符数组
     * @param array
     * @param str
     * @return
     */
    public static String[] addStringToArray(String[] array, String str) {
        if (ObjectUtils.isEmpty(array)) {
            return new String[] {str};
        }
        String[] newArr = new String[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = str;
        return newArr;
    }

    /**
     * string 转 数组
     * @param str
     * @param splitChar
     * @return
     */
    public static String[] toArray(String str, String splitChar) {
        if (isNotEmpty(str) && isNotEmpty(splitChar)){
            return str.split(splitChar);
        }else {
            return EMPTY_STRING_ARRAY;
        }
    }

    public static String[] toArray(String str) {
        return toArray(str, StringUtils.SPLIT_CHAR);
    }

    /**
     * string 转 数组
     * @param str
     * @param splitChar
     * @return
     */
    public static List<String> toList(String str, String splitChar) {
        return Arrays.stream(toArray(str, splitChar))
                .collect(Collectors.toList());
    }

    public static List<String> toList(String str) {
        return toList(str, StringUtils.SPLIT_CHAR);
    }

    /**
     * string 转 数组
     * @param str
     * @param splitChar
     * @return
     */
    public static Set<String> toSet(String str, String splitChar) {
        return Arrays.stream(toArray(str, splitChar))
                .collect(Collectors.toSet());
    }

    public static Set<String> toSet(String str) {
        return toSet(str, StringUtils.SPLIT_CHAR);
    }

    /**
     * string 转 数组
     * @param stringArray
     * @param joinChar
     * @return
     */
    public static String join(String[] stringArray, String joinChar) {
        return Arrays.stream(stringArray).collect(Collectors.joining(joinChar));
    }

    public static String join(String[] stringArray) {
        return join(stringArray, StringUtils.JOIN_CHAR);
    }

    /**
     * 首字母小写
     * @param str
     * @return
     */
    public static String lowerFirst(String str){
        String firstChar = str.substring(0, 1).toLowerCase();
        if (str.length() == 1){
            return firstChar;
        }
        return String.format("%s%s", firstChar, str.substring(1));
    }

    /**
     * 首字母大写
     * @param str
     * @return
     */
    public static String upperFirst(String str){
        String firstChar = str.substring(0, 1).toUpperCase();
        if (str.length() == 1){
            return firstChar;
        }
        return String.format("%s%s", firstChar, str.substring(1));
    }
    /**
     * 截取最前面的字符串
     * @param str
     * @param maxLen
     * @return
     */
    public static String left(String str, int maxLen){
        if (Objects.isNull(str)){
            return null;
        }
        if (str.length() < maxLen){
            return str;
        }
        return str.substring(0, maxLen);
    }


    /**
     * toRight
     * @param str
     * @return
     */
    public static String toRight(String str, int len){
        if (str.length() >= len){
            return str.substring(str.length() - len);
        }
        StringBuffer buffer = new StringBuffer(str);
        for (int i = 0; i <len ; i++) {
            buffer.insert(0, " ");
            if (buffer.length() == len){
                break;
            }
        }
        return buffer.toString();
    }
    /**
     * toLeft
     * @param str
     * @return
     */
    public static String toLeft(String str, int len){
        if (str.length() >= len){
            return str.substring(str.length() - len);
        }
        StringBuffer buffer = new StringBuffer(str);
        for (int i = 0; i <len ; i++) {
            buffer.append(" ");
            if (buffer.length() == len){
                break;
            }
        }
        return buffer.toString();
    }
}
