package com.github.kancyframework.springx.utils;

import java.util.Objects;

/**
 * Asserts
 *
 * @author kancy
 * @date 2020/2/16 5:57
 */
public abstract class Assert {

    /**
     * 断言 expression isTrue
     * @param expression
     * @param message
     */
    public static void isTrue(boolean expression, String message){
        if (!expression){
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言 object isNull
     * @param object
     * @param message
     */
    public static void isNull(Object object, String message){
        if (Objects.nonNull(object)){
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言 object notNull
     * @param object
     * @param message
     */
    public static void notNull(Object object, String message){
        if (Objects.isNull(object)){
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言 object isEmpty
     * @param object
     * @param message
     */
    public static void isEmpty(Object object, String message){
        if (ObjectUtils.isNotEmpty(object)){
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言 object isNotEmpty
     * @param object
     * @param message
     */
    public static void isNotEmpty(Object object, String message){
        if (ObjectUtils.isEmpty(object)){
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言 object isBlank
     * @param object
     * @param message
     */
    public static void isBlank(Object object, String message){
        if (ObjectUtils.isBlank(object)){
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言 object isNotBlank
     * @param object
     * @param message
     */
    public static void isNotBlank(Object object, String message){
        if (ObjectUtils.isNotBlank(object)){
            throw new IllegalArgumentException(message);
        }
    }

}
