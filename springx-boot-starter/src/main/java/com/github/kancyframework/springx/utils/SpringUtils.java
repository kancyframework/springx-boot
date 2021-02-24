package com.github.kancyframework.springx.utils;

import com.github.kancyframework.springx.boot.CommandLineArgument;
import com.github.kancyframework.springx.context.ApplicationContext;
import com.github.kancyframework.springx.context.env.Environment;
import com.github.kancyframework.springx.context.event.ApplicationEvent;

import java.util.Map;
import java.util.Objects;

/**
 * SpringUtils
 *
 * @author kancy
 * @date 2020/2/18 7:36
 */
public abstract class SpringUtils {

    private static ApplicationContext applicationContext;

    private static CommandLineArgument commandLineArgument;

    public static void setApplicationContext(ApplicationContext applicationContext){
        SpringUtils.applicationContext = applicationContext;
    }

    public static void setCommandLineArgument(CommandLineArgument commandLineArgument) {
        SpringUtils.commandLineArgument = commandLineArgument;
    }

    public static CommandLineArgument getCommandLineArgument() {
        return commandLineArgument;
    }

    /**
     * applicationContext
     * @return
     */
    public static ApplicationContext getApplicationContext(){
        return SpringUtils.applicationContext;
    }
    /**
     * getBean
     */
    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }
    /**
     * getBean
     */
    public static <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }
    /**
     * getBean
     */
    public static <T> T getBean(String beanName, Class<T> beanClass) {
        return applicationContext.getBean(beanName, beanClass);
    }
    /**
     * getBeansOfType
     * @param beanClass
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> beanClass){
        return applicationContext.getBeansOfType(beanClass);
    }
    /**
     * 获取环境
     */
    public static Environment getEnvironment(){
        return applicationContext.getEnvironment();
    }
    /**
     * 发布事件
     * @param applicationEvent
     */
    public static void publishEvent(ApplicationEvent applicationEvent){
        applicationContext.publishEvent(applicationEvent);
    }

    public static String getApplicationName(){
        return applicationContext.getEnvironment().getStringProperty("spring.application.name");
    }

    public static String getApplicationName(String def){
        if (Objects.isNull(applicationContext)){
            return def;
        }
        return applicationContext.getEnvironment().getStringProperty("spring.application.name", def);
    }

}
