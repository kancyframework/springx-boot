package com.github.kancyframework.springx.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * BeanUtils
 *
 * @author kancy
 * @date 2021/1/12 16:57
 */
public abstract class BeanUtils {
    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        T target = null;
        try {
            target = ClassUtils.newObject(targetClass);
            copyProperties(source, target);
        } catch (Exception e) {
            ReflectionUtils.handleReflectionException(e);
        }
        return target;
    }

    public static void copyProperties(Object source, Object target) {
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        ReflectionUtils.doWithFields(targetClass, field -> {
            if (Modifier.isStatic(field.getModifiers())){
                return;
            }
            Field sourceField = ReflectionUtils.findField(sourceClass, field.getName(), field.getType());
            if (Objects.nonNull(sourceField)){
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.makeAccessible(sourceField);
                ReflectionUtils.setField(field, target, ReflectionUtils.getField(sourceField, source));
            }
        });
    }

    /**
     * mapToBean
     * @param map
     * @param beanClass
     * @param <T>
     * @return
     */
    public static <T> T mapToBean(Map<String , Object> map, Class<T> beanClass) {
        T bean = null;
        try {
            bean = ClassUtils.newObject(beanClass);
            mapToBean(map, bean);
        } catch (Exception e) {
            ReflectionUtils.handleReflectionException(e);
        }
        return bean;
    }

    /**
     * mapToBean
     * @param source
     * @param target
     */
    public static void mapToBean(Map<String , Object> source, Object target) {
        if (CollectionUtils.isEmpty(source)){
            return;
        }
        Class<?> targetClass = target.getClass();
        ReflectionUtils.doWithFields(targetClass, field -> {
            if (Modifier.isStatic(field.getModifiers())){
                return;
            }
            Object fieldValue = source.get(field.getName());
            fieldValue = ObjectUtils.cast(fieldValue, field.getType());
            if (Objects.nonNull(fieldValue)){
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, target, fieldValue);
            }
        });
    }

    /**
     * beanToMap
     * @param bean
     * @return
     */
    public static Map<String , Object>  beanToMap(Object bean) {
        if (Objects.isNull(bean)){
            return Collections.emptyMap();
        }
        Map<String , Object> map  = new HashMap<>();
        beanToMap(bean, map);
        return map;
    }

    /**
     * beanToMap
     * @param bean
     * @param map
     */
    public static void beanToMap(Object bean, Map<String , Object> map) {
        Class<?> beanClass = bean.getClass();
        ReflectionUtils.doWithFields(beanClass, field -> {
            if (Modifier.isStatic(field.getModifiers())){
                return;
            }
            ReflectionUtils.makeAccessible(field);
            map.put(field.getName(), field.get(bean));
        });
    }

}
