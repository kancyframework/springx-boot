package com.kancy.spring.minidb.registry;

import com.github.kancyframework.springx.context.ApplicationContext;
import com.github.kancyframework.springx.context.DynamicBeanRegistry;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.context.factory.BeanDefinition;
import com.github.kancyframework.springx.utils.ClassUtils;
import com.github.kancyframework.springx.utils.IDUtils;
import com.github.kancyframework.springx.utils.SpringUtils;
import com.kancy.spring.minidb.MapDb;
import com.kancy.spring.minidb.ObjectConfig;
import com.kancy.spring.minidb.ObjectDataManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
     * @param classes
     * @return
     */
    @Override
    public Map<String, BeanDefinition> registerBeans(ApplicationContext applicationContext, Set<Class<?>> classes) {
        Map<String, BeanDefinition> objectConfigBeanMap = new HashMap<>();
        classes.stream()
                .filter(c -> ClassUtils.isAssignableFrom(ObjectConfig.class, c))
                .filter(c -> !c.isAnnotationPresent(Component.class))
                .filter(c -> applicationContext.getBeansOfType(c).isEmpty())
                .forEach(c -> {
                    Class<? extends ObjectConfig> configClass = (Class<? extends ObjectConfig>) c;
                    ObjectConfig objectConfig = ObjectDataManager.load(configClass);
                    BeanDefinition beanDefinition = new BeanDefinition(objectConfig, c);
                    objectConfigBeanMap.put(String.format("objectConfig-%s", IDUtils.getUUIDString()), beanDefinition);
        });

        if (!SpringUtils.existBean(MapDb.class)){
            objectConfigBeanMap.put(String.format("mapDb-%s", IDUtils.getUUIDString()),
                    new BeanDefinition(MapDb.get(), MapDb.class));
        }
        return objectConfigBeanMap;
    }
}
