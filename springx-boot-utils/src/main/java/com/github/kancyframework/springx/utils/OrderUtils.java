package com.github.kancyframework.springx.utils;


import com.github.kancyframework.springx.annotation.Order;

import java.util.*;

/**
 * OrderUtils
 *
 * @author kancy
 * @date 2021/1/9 11:12
 */
public class OrderUtils {
    /**
     * 通过Order注解排序
     * @param collection
     */
    public static  <T> List<T> sort(Collection<T> collection) {
        List<T> list = new ArrayList<T>(collection);
        Collections.sort(list, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return getPriority(o1) - getPriority(o2);
            }
            private int getPriority(Object o1) {
                Order order = o1.getClass().getAnnotation(Order.class);
                // 和Spring 不一样的是，默认的优先级值为0，Spring的是 Integer.MAX_VALUE
                int priority = 0;
                if (Objects.nonNull(order)) {
                    priority = order.value();
                }
                return priority;
            }
        });
        return list;
    }
}
