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
public @interface Import {
    /**
     * 导入类
     * @return
     */
    String[] value() default {};


    /**
     * 导入的类
     * @return
     */
    Class<?>[] classes() default {};
}
