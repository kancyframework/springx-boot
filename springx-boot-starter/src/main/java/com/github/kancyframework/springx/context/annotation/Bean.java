package com.github.kancyframework.springx.context.annotation;

import java.lang.annotation.*;

/**
 * 配置Bean的注解
 *
 * @author kancy
 * @date 2020/2/18 12:12
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {
    /**
     * beanName
     * @return
     */
    String value() default "";
}
