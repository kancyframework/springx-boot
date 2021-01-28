package com.github.kancyframework.springx.context.event;

import com.github.kancyframework.springx.context.annotation.Async;
import com.github.kancyframework.springx.context.ApplicationContext;
import com.github.kancyframework.springx.utils.ClassUtils;
import com.github.kancyframework.springx.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * SimpleApplicationEventMulticaster
 *
 * @author kancy
 * @date 2020/2/18 11:24
 */
public class ApplicationEventMulticaster {

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
            Class<?> tClass = getApplicationEventClass(applicationListener);
            return tClass.isInstance(event);
        }).collect(Collectors.toList());
        return listeners;
    }

    private Class<?> getApplicationEventClass(ApplicationListener applicationListener){
        ParameterizedType parameterizedType = (ParameterizedType) applicationListener.getClass().getGenericInterfaces()[0];
        return (Class<?>) parameterizedType.getActualTypeArguments()[0];
    }
}
