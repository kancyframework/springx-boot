package com.github.kancyframework.springx.boot;

import com.github.kancyframework.springx.context.annotation.Configuration;

import java.lang.annotation.*;

/**
 * SpringBootApplication
 *
 * @author kancy
 * @date 2020/2/18 12:12
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
public @interface SpringBootApplication {
    String[] basePackages() default {};
}
