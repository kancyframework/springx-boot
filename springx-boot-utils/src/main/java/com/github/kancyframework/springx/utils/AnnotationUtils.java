package com.github.kancyframework.springx.utils;

import com.github.kancyframework.springx.annotation.Order;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * AnnotationUtils
 *
 * @author kancy
 * @date 2021/1/12 16:57
 */
@Order(9)
public abstract class AnnotationUtils {

    public static <T> T toBean(AnnotatedElement annotatedElement , Class<? extends Annotation> annotationClass, Class<T> propertyClass){
        return toBean(findAnnotation(annotatedElement, annotationClass), propertyClass);
    }

    public static Map<String, Object> toMap(AnnotatedElement annotatedElement , Class<? extends Annotation> annotationClass){
        return toMap(findAnnotation(annotatedElement, annotationClass));
    }

    public static <T> T toBean(Annotation annotation, Class<T> propertyClass){
       return BeanUtils.mapToBean(toMap(annotation), propertyClass);
    }

    public static Map<String, Object> toMap(Annotation annotation){
        Map<String, Object> map = new HashMap<>();
        ReflectionUtils.doWithMethods(annotation.getClass(), method -> {
            Object value = ReflectionUtils.invokeMethod(annotation, method.getName());
            map.put(method.getName(), value);
        });
        return map;
    }

    public static <T extends Annotation> T findAnnotation(AnnotatedElement annotatedElement , Class<T> annotationClass){
        if (annotatedElement instanceof Class){
            Class<?> cls = Class.class.cast(annotatedElement);
            if (cls.isAnnotationPresent(annotationClass)){
                return cls.getDeclaredAnnotation(annotationClass);
            } else {
                if (Objects.isNull(cls.getSuperclass())){
                    return null;
                }
                return findAnnotation(cls.getSuperclass(), annotationClass);
            }
        } else {
            return annotatedElement.getAnnotation(annotationClass);
        }
    }

    public static <T> T getProperty(AnnotatedElement annotatedElement , Class<? extends Annotation> annotationClass, String propertyName, Class<T> valueType){
        Annotation annotation = findAnnotation(annotatedElement, annotationClass);
        if (Objects.isNull(annotation)){
            return null;
        }
        return (T) ReflectionUtils.invokeMethod(annotation, propertyName);
    }

    public static <T> T getProperty(AnnotatedElement annotatedElement, Class<? extends Annotation> annotationClass, String propertyName){
        return (T) getProperty(annotatedElement, annotationClass, propertyName, Object.class);
    }

    public static <T> T getValue(AnnotatedElement annotatedElement, Class<? extends Annotation> annotationClass, Class<T> valueType){
        return getProperty(annotatedElement, annotationClass, "value", valueType);
    }
    public static <T> T getValue(AnnotatedElement annotatedElement, Class<? extends Annotation> annotationClass){
        return (T) getValue(annotatedElement, annotationClass,  Object.class);
    }

}
