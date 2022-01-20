package com.github.kancyframework.springx.context.task;

import com.github.kancyframework.springx.annotation.Order;
import com.github.kancyframework.springx.context.ApplicationContext;
import com.github.kancyframework.springx.context.DynamicBeanRegistry;
import com.github.kancyframework.springx.context.factory.BeanDefinition;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TaskExecutorImportSelector
 *
 * @author huangchengkang
 * @date 2022/1/21 1:20
 */
@Order(value = Integer.MAX_VALUE)
public class TaskExecutorDynamicBeanRegistry implements DynamicBeanRegistry {

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
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        BeanDefinition beanDefinition = new BeanDefinition(executorService, executorService.getClass());
        Map<String, BeanDefinition> beans = new HashMap<>();
        beans.put("taskExecutor", beanDefinition);
        return beans;
    }
}
