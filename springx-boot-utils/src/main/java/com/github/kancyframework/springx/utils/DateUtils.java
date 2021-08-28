package com.github.kancyframework.springx.utils;

import java.text.ParseException;
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
    public static Date toDate(String dateStr){
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e1) {
            try {
                date = new SimpleDateFormat("yyyy/MM/dd").parse(dateStr);
            } catch (ParseException e2) {
                try {
                    date = new SimpleDateFormat("yyyyMMdd").parse(dateStr);
                } catch (ParseException e3) {
                    try {
                        date = new SimpleDateFormat("yyyy年MM月dd日").parse(dateStr);
                    } catch (ParseException e4) {
                        e4.printStackTrace();
                    }
                }
            }
        }
        return date;
    }
    public static Date toDateTime(String dateStr) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
        } catch (ParseException e1) {
            try {
                date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateStr);
            } catch (ParseException e2) {
                e2.printStackTrace();
            }
        }
        return date;
    }
    public static Date toTime(String dateStr , String format) {
        try {
            return new SimpleDateFormat(format).parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
}
