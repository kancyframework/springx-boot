package com.kancy.spring.minidb;

import com.github.kancyframework.springx.log.Logger;
import com.github.kancyframework.springx.log.LoggerFactory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * SaveDataMethodInterceptor
 *
 * @author kancy
 * @date 2021/1/12 13:27
 */
public class ObjectDataMethodInterceptor implements MethodInterceptor , Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(ObjectDataMethodInterceptor.class);

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if (Objects.equals(method.getDeclaringClass(), Object.class)){
            return methodProxy.invokeSuper(obj, args);
        }
        if (method.getParameterCount() == 0){
            return methodProxy.invokeSuper(obj, args);
        }
        if (!(obj instanceof ObjectData)){
            return methodProxy.invokeSuper(obj, args);
        }
        Object result = methodProxy.invokeSuper(obj, args);
        boolean save = ((ObjectData) obj).save();
        if (save){
            log.debug("Refresh minidb {} object data success!", obj.getClass().getSimpleName());
        }
        return result;
    }
}
