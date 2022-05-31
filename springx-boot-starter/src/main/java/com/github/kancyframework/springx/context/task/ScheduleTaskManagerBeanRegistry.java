package com.github.kancyframework.springx.context.task;

import com.github.kancyframework.springx.annotation.Order;
import com.github.kancyframework.springx.context.ApplicationContext;
import com.github.kancyframework.springx.context.DynamicBeanRegistry;
import com.github.kancyframework.springx.context.factory.BeanDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * TaskExecutorImportSelector
 *
 * @author huangchengkang
 * @date 2022/1/21 1:20
 */
@Order(value = Integer.MAX_VALUE)
public class ScheduleTaskManagerBeanRegistry implements DynamicBeanRegistry {

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
        ScheduleTaskManager scheduleTaskManager = new ScheduleTaskManager();
        BeanDefinition scheduleTaskManagerBeanDefinition = new BeanDefinition(scheduleTaskManager, scheduleTaskManager.getClass());
        Map<String, BeanDefinition> beans = new HashMap<>();
        beans.put("scheduleTaskManager", scheduleTaskManagerBeanDefinition);
        return beans;
    }
}
