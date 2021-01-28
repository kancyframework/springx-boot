package com.github.kancyframework.springx.context.factory;

import java.util.Map;

/**
 * ListableBeanFactory
 *
 * @author kancy
 * @date 2020/2/18 12:12
 */
public interface ListableBeanFactory extends BeanFactory {
    /**
     * 获取这个类型的所有Bean
     * @param beanType
     * @param <T>
     * @return
     */
    <T> Map<String, T> getBeansOfType(Class<T> beanType);

}
