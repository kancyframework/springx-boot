package com.github.kancyframework.springx.utils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * ObjectUtils
 *
 * @author kancy
 * @date 2020/2/16 6:07
 */
public abstract class ObjectUtils {

    /**
     * 是否为空
     * @param object
     * @return
     */
    public static boolean isEmpty(Object object){
        if (Objects.isNull(object)){
            return true;
        }
        if (object instanceof String){
            return StringUtils.isEmpty((String) object);
        }else if (object instanceof Collection){
            return CollectionUtils.isEmpty((Collection) object);
        }else if (object instanceof Map){
            return CollectionUtils.isEmpty((Map) object);
        }else if (object.getClass().isArray()){
            return Objects.equals(Array.getLength(object), 0);
        }
        return false;
    }

    /**
     * 是否不为空
     * @param object
     * @return
     */
    public static boolean isNotEmpty(Object object){
        return !isEmpty(object);
    }

    /**
     * 是否为空
     * @param object
     * @return
     */
    public static boolean isBlank(Object object){
        if (Objects.isNull(object)){
            return true;
        }
        if (object instanceof String){
            return StringUtils.isBlank((String) object);
        } else {
            return false;
        }
    }
    /**
     * 是否不为空
     * @param object
     * @return
     */
    public static boolean isNotBlank(Object object){
        return !isBlank(object);
    }
    /**
     * 对象转换类型
     * @param value
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T cast(Object value, Class<T> type) {
        return cast(value, type, null);
    }

    /**
     * 对象转换类型
     * 无法转换时设置为默认值
     * @param value
     * @param type
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T> T cast(Object value, Class<T> type, T defaultValue) {
        if (Objects.isNull(value)){
            return defaultValue;
        }
        if (type.isInstance(value)){
            return type.cast(value);
        }

        Object returnValue = null;
        String stringValue = String.valueOf(value);
        try {
            if (String.class.equals(type)){
                returnValue = stringValue;
            } else if (StringBuffer.class.equals(type)){
                returnValue = new StringBuffer(stringValue);
            } else if (StringBuilder.class.equals(type)){
                returnValue = new StringBuilder(stringValue);
            } else if (BigDecimal.class.equals(type)){
                returnValue = new BigDecimal(stringValue);
            } else if (Integer.class.equals(type) || int.class.equals(type)){
                returnValue = Integer.parseInt(stringValue);
            } else if (Double.class.equals(type) || double.class.equals(type)){
                returnValue = Double.parseDouble(stringValue);
            } else if (Long.class.equals(type) || long.class.equals(type)){
                returnValue = Long.parseLong(stringValue);
            } else if (Float.class.equals(type) || float.class.equals(type)){
                returnValue = Float.parseFloat(stringValue);
            } else if (Short.class.equals(type) || short.class.equals(type)){
                returnValue = Short.parseShort(stringValue);
            } else if (Boolean.class.equals(type) || boolean.class.equals(type)){
                returnValue = Boolean.parseBoolean(stringValue);
            } else if (Duration.class.equals(type)){
                returnValue = Duration.parse(stringValue);
            } else if (LocalDate.class.equals(type)){
                try {
                    returnValue = LocalDate.parse(stringValue, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (Exception e) {
                    returnValue = LocalDate.parse(stringValue, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                }
            } else if (LocalDateTime.class.equals(type)){
                try {
                    returnValue = LocalDateTime.parse(stringValue, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                } catch (Exception e) {
                    returnValue = LocalDateTime.parse(stringValue, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
                }
            }  else if (LocalTime.class.equals(type)){
                try {
                    returnValue = LocalTime.parse(stringValue, DateTimeFormatter.ofPattern("HH:mm:ss"));
                } catch (Exception e) {
                    returnValue = LocalTime.parse(stringValue, DateTimeFormatter.ofPattern("HH:mm:ss"));
                }
            } else {
                return defaultValue;
            }
        } catch (Exception e) {
            return defaultValue;
        }
        return (T) returnValue;
    }
}
