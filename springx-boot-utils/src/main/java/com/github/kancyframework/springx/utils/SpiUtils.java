package com.github.kancyframework.springx.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * SpiUtils
 *
 * @author kancy
 * @date 2021/1/9 11:09
 */
public class SpiUtils {

    /**
     * 查找Spi服务
     * @param serviceClass
     * @param <S>
     * @return
     */
    public static <S> List<S> findServices(Class<S> serviceClass) {
        return findServices(serviceClass, Thread.currentThread().getContextClassLoader());
    }

    /**
     * 查找Spi服务
     * @param serviceClass
     * @param loader
     * @param <S>
     * @return
     */
    public static <S> List<S> findServices(Class<S> serviceClass, ClassLoader loader) {
        ServiceLoader<S> serviceLoader = ServiceLoader.load(serviceClass, loader);
        ArrayList<S> list = new ArrayList<>();
        serviceLoader.forEach(service -> list.add(service));
       return OrderUtils.sort(list);
    }

}
