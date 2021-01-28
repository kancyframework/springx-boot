package com.github.kancyframework.springx.context.annotation;

import java.lang.annotation.*;

/**
 * Component
 *
 * @author kancy
 * @date 2020/2/18 12:12
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Component {
    /**
     * beanName
     * @return
     */
    String value() default "";
}