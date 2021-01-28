package com.github.kancyframework.springx.utils;

import java.lang.reflect.Array;
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
}
