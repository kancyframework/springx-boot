package com.github.kancyframework.springx.swing.action;

import com.github.kancyframework.springx.context.annotation.Component;

import java.lang.annotation.*;

/**
 * 配置Bean的注解
 *
 * @author kancy
 * @date 2020/2/18 12:12
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface ActionCommand {
    /**
     * beanName
     * @return
     */
    String[] value() default {};
}
