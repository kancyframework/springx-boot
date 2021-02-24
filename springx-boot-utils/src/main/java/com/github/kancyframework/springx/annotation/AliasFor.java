package com.github.kancyframework.springx.annotation;

import java.lang.annotation.*;

/**
 * 注解属性别名设置
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface AliasFor {
    @AliasFor("attribute")
    String value() default "";

    @AliasFor("value")
    String attribute() default "";

    Class<? extends Annotation> annotation() default Annotation.class;
}