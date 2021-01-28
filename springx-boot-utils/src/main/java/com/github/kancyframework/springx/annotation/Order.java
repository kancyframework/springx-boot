package com.github.kancyframework.springx.annotation;

import java.lang.annotation.*;

/**
 * Order
 *
 * @author kancy
 * @date 2020/2/18 20:59
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface Order {
    /**
     * 排序值越大，优先级越低
     * 和Spring 不一样的是，默认的优先级值为0
     * Spring的是 Integer.MAX_VALUE
     * @return
     */
    int value() default 0;
}