package com.github.kancyframework.springx.context.event;

import com.github.kancyframework.springx.context.ApplicationContext;
import com.github.kancyframework.springx.context.annotation.Async;
import com.github.kancyframework.springx.utils.ClassUtils;
import com.github.kancyframework.springx.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * SimpleApplicationEventMulticaster
 *
 * @author kancy
 * @date 2020/2/18 11:24
 */
public class ApplicationEventMulticaster {

    private final Map<Class, Class> applicationListenerGenericMap = new HashMap<>();

    private final ApplicationContext applicationContext;

    public ApplicationEventMulticaster(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 广播事件
     * @param event
     */
    public void multicastEvent(final ApplicationEvent event) {
        if (Objects.isNull(event)){
            throw new IllegalArgumentException("event object is null.");
        }
        //获得监听器集合，遍历监听器，可支持同步和异步的广播事件
        for (final ApplicationListener listener : getApplicationListeners(event)) {
            Class<?> listenerClass = listener.getClass();
            Executor executor = null;
            if (ClassUtils.isAnnotationPresentOnClass(listenerClass, Async.class)){
                Async annotation = listenerClass.getAnnotation(Async.class);
                executor = getTaskExecutor(annotation.value());
            }else {
                Method listenerMethod = ReflectionUtils.findMethod(listenerClass,"onApplicationEvent", ApplicationEvent.class);
                if (listenerMethod.isAnnotationPresent(Async.class)){
                    executor = getTaskExecutor(listenerMethod.getAnnotation(Async.class).value());
                }
            }
            if (Objects.nonNull(executor)){
                executor.execute(() -> listener.onApplicationEvent(event));
            }else {
                listener.onApplicationEvent(event);
            }
        }
    }

    private Executor getTaskExecutor(String beanName) {
        return applicationContext.getBean(beanName, Executor.class);
    }

    private Collection<ApplicationListener> getApplicationListeners(ApplicationEvent event) {
        Map<String, ApplicationListener> beanMaps = applicationContext.getBeansOfType(ApplicationListener.class);
        Collection<ApplicationListener> values = beanMaps.values();
        List<ApplicationListener> listeners = values.stream().filter(applicationListener -> {
            // 过滤不关系的事件
            Class<?> tClass = getApplicationEventClass(applicationListener);
            return Objects.nonNull(tClass) && tClass.isInstance(event);
        }).collect(Collectors.toList());
        return listeners;
    }

    private Class<?> getApplicationEventClass(ApplicationListener applicationListener){
        Class<?> searchClass = applicationListener.getClass();
        if (applicationListenerGenericMap.containsKey(searchClass)){
            return applicationListenerGenericMap.get(searchClass);
        }
        Type[] genericInterfaces = searchClass.getGenericInterfaces();
        if (genericInterfaces.length == 0){
            searchClass = (Class<?>) searchClass.getGenericSuperclass();
            if (Objects.nonNull(searchClass)){
                genericInterfaces = searchClass.getGenericInterfaces();
                ParameterizedType parameterizedType = null;
                if (genericInterfaces.length != 0){
                    parameterizedType = (ParameterizedType) genericInterfaces[0];
                }else {
                    parameterizedType = (ParameterizedType) searchClass.getGenericSuperclass();
                }
                if (Objects.nonNull(parameterizedType)){
                    Class<?> paramType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                    applicationListenerGenericMap.put(applicationListener.getClass(), paramType);
                    return paramType;
                }
            }
        }

        if (genericInterfaces.length == 0){
            applicationListenerGenericMap.put(applicationListener.getClass(), null);
            return null;
        }
        ParameterizedType parameterizedType = (ParameterizedType) genericInterfaces[0];
        Class<?> paramType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
        applicationListenerGenericMap.put(applicationListener.getClass(), paramType);
        return paramType;
    }

    private Class sss(Class searchClass){
        Type[] genericInterfaces = searchClass.getGenericInterfaces();
        if (genericInterfaces.length == 0){
            searchClass = (Class<?>) searchClass.getGenericSuperclass();
            if (Objects.nonNull(searchClass)){
                genericInterfaces = searchClass.getGenericInterfaces();
                ParameterizedType parameterizedType = null;
                if (genericInterfaces.length != 0){
                    parameterizedType = (ParameterizedType) genericInterfaces[0];
                }else {
                    parameterizedType = (ParameterizedType) searchClass.getGenericSuperclass();
                }
                if (Objects.nonNull(parameterizedType)){
                    Class<?> paramType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                    return paramType;
                }else {
                }
            }
        }
        ParameterizedType parameterizedType = (ParameterizedType) genericInterfaces[0];
        Class<?> paramType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
        return paramType;
    }
}
