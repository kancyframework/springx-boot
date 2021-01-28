package com.github.kancyframework.springx.context.annotation;

import java.lang.annotation.*;

/**
 * Profile
 *
 * @author kancy
 * @date 2020/2/18 20:33
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Profile {
    String[] value();
}
