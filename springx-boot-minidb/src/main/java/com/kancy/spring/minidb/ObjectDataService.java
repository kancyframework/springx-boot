package com.kancy.spring.minidb;

import com.github.kancyframework.springx.utils.ClassUtils;
import com.github.kancyframework.springx.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * ObjectDataFactory
 *
 * @author kancy
 * @date 2021/1/12 14:10
 */
public class ObjectDataService {

    private static Method createProxyMethod;
    private static Method isEnhancedMethod;
    private static Object objectDataMethodInterceptor;

    static {
        try {
            Class<?> enhancerClass = Class.forName("net.sf.cglib.proxy.Enhancer");
            Class<?> callbackClass = Class.forName("net.sf.cglib.proxy.Callback");
            createProxyMethod = ReflectionUtils.findMethod(enhancerClass, "create", Class.class, callbackClass);
            isEnhancedMethod = ReflectionUtils.findMethod(enhancerClass, "isEnhanced", Class.class);
            objectDataMethodInterceptor = ClassUtils.newObject("com.kancy.spring.minidb.ObjectDataMethodInterceptor");
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     *
     * @param dataClass
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T extends ObjectConfig> T initObjectData(Class<T> dataClass) throws Exception {
        T objectDataProxy = null;
        if (isUseProxy(dataClass)){
            objectDataProxy = (T) createProxyMethod.invoke(createProxyMethod.getDeclaringClass(), dataClass, objectDataMethodInterceptor);
        }else {
            objectDataProxy = ClassUtils.newObject(dataClass);
        }
        return objectDataProxy;
    }

    public static boolean isUseProxy(Class<?> dataClass){
        return !Modifier.isFinal(dataClass.getModifiers())
                && Objects.nonNull(createProxyMethod)
                && Objects.nonNull(objectDataMethodInterceptor);
    }

    public static boolean isProxy(Object object){
        return isProxy(object.getClass());
    }

    public static boolean isProxy(Class<?> cls){
        try {
            return (boolean) isEnhancedMethod.invoke(createProxyMethod.getDeclaringClass(), cls);
        } catch (Exception e) {
            return false;
        }
    }

}
