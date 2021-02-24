package com.github.kancyframework.springx.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
/**
 * DateUtils
 *
 * @author kancy
 * @date 2020/2/16 6:05
 */
public abstract class DateUtils {
    public static String getDatePathStr(LocalDate date){
        return getDateStr(date, "yyyy/MM/dd");
    }
    public static String getDateStr(LocalDate date){
        return getDateStr(date, "yyyy-MM-dd");
    }
    public static String getDateStr(LocalDate date, String format){
        return date.format(DateTimeFormatter.ofPattern(format));
    }
    public static String getDateStr(LocalDateTime date){
        return getDateStr(date, "yyyy-MM-dd HH:mm:ss");
    }
    public static String getDateStr(LocalDateTime date, String format){
        return date.format(DateTimeFormatter.ofPattern(format));
    }
    public static String getDateStr(Date date, String format){
        return new SimpleDateFormat(format).format(date);
    }
    public static String getNowStr(String format){
        return new SimpleDateFormat(format).format(new Date());
    }
    public static String getNowTimestampStr(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }
    public static String getNowStr(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
    public static String getNowPathStr(){
        return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    }
}
