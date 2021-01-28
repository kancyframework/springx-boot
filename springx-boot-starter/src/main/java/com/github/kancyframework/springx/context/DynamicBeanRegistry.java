package com.github.kancyframework.springx.context;

import com.github.kancyframework.springx.context.factory.BeanDefinition;

import java.util.Map;

/**
 * DynamicBeanRegistry
 *
 * @author kancy
 * @date 2021/1/9 12:29
 */
public interface DynamicBeanRegistry {

    /**
     * 注册Beans
     * key -> beanName
     * value -> BeanDefinition
     * @param applicationContext
     * @return
     */
    Map<String, BeanDefinition> registerBeans(ApplicationContext applicationContext) ;
}
