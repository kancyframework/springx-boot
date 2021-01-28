package com.github.kancyframework.springx.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * BeanUtils
 *
 * @author kancy
 * @date 2021/1/12 16:57
 */
public abstract class BeanUtils {
    public static <T> T copy(Object source, Class<T> targetClass) {
        T target = null;
        try {
            target = ClassUtils.newObject(targetClass);
            copy(source, target);
        } catch (Exception e) {
            ReflectionUtils.handleReflectionException(e);
        }
        return target;
    }

    public static void copy(Object source, Object target) {
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
}
