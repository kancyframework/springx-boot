package com.github.kancyframework.springx.context.factory;

/**
 * BeanFactory
 *
 * @author kancy
 * @date 2020/2/18 12:12
 */
public interface BeanFactory {
    /**
     * 通过beanName获取bean
     * @param beanName bean名称
     * @return
     */
    Object getBean(String beanName);

    /**
     * 获取bean
     * @param beanName
     * @param requiredType
     * @param <T>
     * @return
     */
    <T> T getBean(String beanName, Class<T> requiredType);

    /**
     * 通过bean类型获取bean
     * @param beanClass
     * @param <T>
     * @return
     */
    <T> T getBean(Class<T> beanClass);

    /**
     * 容器是否包含Bean
     * @param beanName
     * @return
     */
    boolean containsBean(String beanName);
}
