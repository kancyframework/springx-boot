package com.github.kancyframework.springx.context.annotation;

import java.lang.annotation.*;

/**
 * Configuration
 * 标明配置类，用来配置@Bean
 *
 * @author kancy
 * @date 2021/1/9 1:55
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Component
public @interface Configuration {
    /**
     * beanName
     * @return
     */
    String value() default "";
}
