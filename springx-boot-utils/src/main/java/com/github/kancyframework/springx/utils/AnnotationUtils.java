package com.github.kancyframework.springx.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * AnnotationUtils
 *
 * @author kancy
 * @date 2021/1/12 16:57
 */
public abstract class AnnotationUtils {

    public static Annotation findAnnotation(Class<?> cls , Class<? extends Annotation> annotationClass){
        if (cls.isAnnotationPresent(annotationClass)){
            return cls.getDeclaredAnnotation(annotationClass);
        } else {
            if (Objects.isNull(cls.getSuperclass())){
                return null;
            }
            return findAnnotation(cls.getSuperclass(), annotationClass);
        }
    }

    public static <T> T getProperty(Class<?> cls , Class<? extends Annotation> annotationClass, String propertyName, Class<T> valueType){
        Annotation annotation = findAnnotation(cls, annotationClass);
        if (Objects.isNull(annotation)){
            return null;
        }
        return (T) ReflectionUtils.invokeMethod(annotation, propertyName);
    }

    public static Object getProperty(Class<?> cls , Class<? extends Annotation> annotationClass, String propertyName){
        return getProperty(cls, annotationClass, propertyName, Object.class);
    }

    public static <T> T getValue(Class<?> cls , Class<? extends Annotation> annotationClass, Class<T> valueType){
        return getProperty(cls, annotationClass, "value", valueType);
    }
    public static Object getValue(Class<?> cls , Class<? extends Annotation> annotationClass){
        return getValue(cls, annotationClass,  Object.class);
    }

    public static <T> T getProperty(Method method , Class<? extends Annotation> annotationClass, String propertyName, Class<T> valueType){
        if (!method.isAnnotationPresent(annotationClass)){
            return null;
        }
        return (T) ReflectionUtils.invokeMethod(method.getAnnotation(annotationClass), propertyName);
    }

    public static Object getProperty(Method method, Class<? extends Annotation> annotationClass, String propertyName){
        return getProperty(method, annotationClass, propertyName, Object.class);
    }

    public static <T> T getValue(Method method, Class<? extends Annotation> annotationClass, Class<T> valueType){
        return getProperty(method, annotationClass, "value", valueType);
    }
    public static Object getValue(Method method, Class<? extends Annotation> annotationClass){
        return getValue(method, annotationClass,  Object.class);
    }

    public static <T> T getProperty(Field field , Class<? extends Annotation> annotationClass, String propertyName, Class<T> valueType){
        if (!field.isAnnotationPresent(annotationClass)){
            return null;
        }
        return (T) ReflectionUtils.invokeMethod(field.getAnnotation(annotationClass), propertyName);
    }
    public static Object getProperty(Field field, Class<? extends Annotation> annotationClass, String propertyName){
        return getProperty(field, annotationClass, propertyName, Object.class);
    }

    public static <T> T getValue(Field field, Class<? extends Annotation> annotationClass, Class<T> valueType){
        return getProperty(field, annotationClass, "value", valueType);
    }
    public static Object getValue(Field field, Class<? extends Annotation> annotationClass){
        return getValue(field, annotationClass,  Object.class);
    }
}
