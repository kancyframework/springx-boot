package com.github.kancyframework.springx.context.factory;

/**
 * Component
 *
 * @author kancy
 * @date 2020/2/18 12:12
 */
public class BeanDefinition {
    private Object object;

    private Class<?> clazz;

    public BeanDefinition(Object object, Class<?> clazz) {
        this.object = object;
        this.clazz = clazz;
    }

    public Object getObject() {
        return object;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
