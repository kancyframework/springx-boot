package com.github.kancyframework.springx.context.annotation;

import java.lang.annotation.*;

/**
 * Autowired
 *
 * @author kancy
 * @date 2020/2/18 12:12
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Async {
    String value() default "taskExecutor";
}