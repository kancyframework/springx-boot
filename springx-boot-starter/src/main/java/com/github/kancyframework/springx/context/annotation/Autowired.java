package com.github.kancyframework.springx.context.annotation;

import java.lang.annotation.*;

/**
 * Autowired
 *
 * @author kancy
 * @date 2020/2/18 12:12
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    /**
     * 必须
     * @return
     */
    boolean required() default true;

    /**
     * beanName
     * this field is moved from @Qualifier to here for simplicity
     * @return
     */
    String value() default "";
}