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
public @interface Value {
    /**
     * env property name , like ${user.name}
     * @return
     */
    String value();
}