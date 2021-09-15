package com.kancy.spring.minidb.registry;

import com.github.kancyframework.springx.context.ApplicationContext;
import com.github.kancyframework.springx.context.DynamicBeanRegistry;
import com.github.kancyframework.springx.context.factory.BeanDefinition;
import com.kancy.spring.minidb.ObjectConfig;
import com.kancy.spring.minidb.ObjectDataManager;

import java.util.HashMap;
import java.util.Map;

/**
 * ObjectConfigBeanRegistry
 *
 * @author huangchengkang
 * @date 2021/9/16 2:24
 */
public class ObjectConfigBeanRegistry implements DynamicBeanRegistry {
    /**
     * 注册Beans
     * key -> beanName
     * value -> BeanDefinition
     *
     * @param applicationContext
     * @return
     */
    @Override
    public Map<String, BeanDefinition> registerBeans(ApplicationContext applicationContext) {
        Map<String, BeanDefinition> objectConfigBeanMap = new HashMap<>();
        applicationContext.getBeansOfType(ObjectConfig.class).forEach((beanName, config) ->{
            Class<? extends ObjectConfig> configClass = config.getClass();
            ObjectConfig objectConfig = ObjectDataManager.load(configClass);
            BeanDefinition beanDefinition = new BeanDefinition(objectConfig, config.getClass());
            objectConfigBeanMap.put(beanName, beanDefinition);
        });
        return objectConfigBeanMap;
    }
}
