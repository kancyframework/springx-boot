package com.github.kancyframework.springx.utils;

import com.github.kancyframework.springx.annotation.AliasFor;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AnnotationUtils
 *
 * @author kancy
 * @date 2021/1/12 16:57
 */
public abstract class AnnotationUtils {

    private static final Map<String, Annotation> annotationCache = new ConcurrentHashMap<>();

    /**
     * 查找注解 (自动识别注解上的注解和父类的注解，支持别名设置)
     * @see AliasFor
     * @param annotatedElement
     * @param annotationClass
     * @param <T>
     * @return
     */
    public static <T extends Annotation> T findAnnotation(AnnotatedElement annotatedElement , Class<T> annotationClass){

        // 从注解缓存中查找
        String annotationCacheKey = String.format("%s@%s", annotatedElement, annotationClass.getName());
        if (annotationCache.containsKey(annotationCacheKey)){
            Annotation annotation = annotationCache.get(annotationCacheKey);
            if (Objects.isNull(annotation)){
                return null;
            }
            return annotationClass.cast(annotation);
        }

        // 当前类能查找到注解
        if (annotatedElement.isAnnotationPresent(annotationClass)){
            T targetAnnotation = annotatedElement.getAnnotation(annotationClass);
            T annotationProxy = getAnnotationProxy(annotationClass, targetAnnotation, targetAnnotation);
            // 加入缓存
            annotationCache.putIfAbsent(annotationCacheKey, annotationProxy);
            return annotationProxy;
        }

        // 在注解上找注解
        T targetAnnotation = null;
        Annotation[] annotations = annotatedElement.getAnnotations();
        for (Annotation dataAnnotation : annotations) {
            // 自定义的注解，不可能出现在jdk注解中
            String packageName = dataAnnotation.annotationType().getPackage().getName();
            if (packageName.startsWith("java.lang")){
                continue;
            }
            targetAnnotation = dataAnnotation.annotationType().getAnnotation(annotationClass);
            if (targetAnnotation == null) {
                // 从注解的上层继续查找
                targetAnnotation = findAnnotation(dataAnnotation.annotationType(), annotationClass);
            }
            if (targetAnnotation != null){
                // 重新生成注解的代理对象
                T annotationProxy = getAnnotationProxy(annotationClass, targetAnnotation, dataAnnotation);
                // 加入缓存
                annotationCache.putIfAbsent(annotationCacheKey, annotationProxy);
                return annotationProxy;
            }
        }

        // 从类的父类上递归查找
        if (annotatedElement instanceof Class){
            Class<?> cls = Class.class.cast(annotatedElement);
            if (Objects.nonNull(cls.getSuperclass())){
                T annotation = findAnnotation(cls.getSuperclass(), annotationClass);
                // 加入缓存
                annotationCache.putIfAbsent(annotationCacheKey, annotation);
                return annotation;
            }
        }
        return null;
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

    private static <T extends Annotation> T getAnnotationProxy(Class<T> annotationClass, T targetAnnotation, Annotation dataAnnotation) {
        // 重新生成注解的代理对象
        HashMap<String, Method> dataAnnotationMethodMap = new HashMap<>();
        Method[] annotationMethods = dataAnnotation.annotationType().getDeclaredMethods();
        for (Method annotationMethod : annotationMethods) {
            if (annotationMethod.isAnnotationPresent(AliasFor.class)) {
                AliasFor aliasFor = annotationMethod.getAnnotation(AliasFor.class);
                String attributeName = aliasFor.value();
                if (StringUtils.isBlank(attributeName)) {
                    attributeName = aliasFor.attribute();
                }
                if (StringUtils.isBlank(attributeName)) {
                    attributeName = annotationMethod.getName();
                }
                String methodKey = String.format("%s@%s", aliasFor.annotation().getName(), attributeName);
                dataAnnotationMethodMap.put(methodKey, annotationMethod);
            }
        }
        return annotationClass.cast(createAnnotationProxy(targetAnnotation, dataAnnotation, dataAnnotationMethodMap));
    }

    /**
     * 创建注解代理类对象
     * @param targetAnnotation
     * @param dataAnnotation
     * @param dataAnnotationMethodMap
     * @return
     */
    private static Object createAnnotationProxy(Annotation targetAnnotation, Annotation dataAnnotation,
                                                                  Map<String, Method> dataAnnotationMethodMap) {
        Class<? extends Annotation> annotationType = targetAnnotation.annotationType();
        Class<?>[] exposedInterfaces = new Class[]{annotationType};
        return Proxy.newProxyInstance(targetAnnotation.getClass().getClassLoader(), exposedInterfaces, (proxy, method, args) -> {
            Method targetMethod = null;
            if (targetAnnotation == dataAnnotation){
                Object invoke = method.invoke(targetAnnotation);
                // TODO 判断值为空
                if (Objects.equals(invoke, "")){
                    targetMethod = dataAnnotationMethodMap.get(String.format("%s@%s", Annotation.class.getName(), method.getName()));
                } else {
                    return invoke;
                }
            } else {
                String methodKey = String.format("%s@%s", targetAnnotation.annotationType().getName(), method.getName());
                targetMethod = dataAnnotationMethodMap.get(methodKey);
                if (Objects.isNull(targetMethod)){
                    methodKey = String.format("%s@%s", Annotation.class.getName(), method.getName());
                    targetMethod = dataAnnotationMethodMap.get(methodKey);
                }
            }
            if (Objects.nonNull(targetMethod)){
                return targetMethod.invoke(dataAnnotation);
            }
            return method.invoke(targetAnnotation, args);
        });
    }

}
