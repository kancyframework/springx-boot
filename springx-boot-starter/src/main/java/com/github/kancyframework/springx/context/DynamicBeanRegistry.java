package com.github.kancyframework.springx.context;

import com.github.kancyframework.springx.context.factory.BeanDefinition;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

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
    default Map<String, BeanDefinition> registerBeans(ApplicationContext applicationContext) {
        return Collections.emptyMap();
    }

    /**
     * 注册Beans
     * key -> beanName
     * value -> BeanDefinition
     * @param applicationContext
     * @return
     */
    default Map<String, BeanDefinition> registerBeans(ApplicationContext applicationContext, Set<Class<?>> classes){
        return registerBeans(applicationContext);
    }
}
