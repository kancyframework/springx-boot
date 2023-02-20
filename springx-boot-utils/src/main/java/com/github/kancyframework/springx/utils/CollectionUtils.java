package com.github.kancyframework.springx.utils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * CollectionUtils
 *
 * @author kancy
 * @date 2020/2/16 6:05
 */
public abstract class CollectionUtils {

    private static final Object[] EMPTY_OBJECT_ARRAY = {};
    private static final String[] EMPTY_STR_ARRAY = {};

    /**
     * 是否为空
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection<?> collection) {
        return Objects.isNull(collection) || collection.isEmpty();
    }
    /**
     * 是否为空
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return Objects.isNull(map) || map.isEmpty();
    }

    /**
     * 集合转数组
     * @param collection
     * @return
     */
    public static Object[] toArray(Collection<?> collection) {
        if (isEmpty(collection)){
            return EMPTY_OBJECT_ARRAY;
        }else {
            Object[] objects = new Object[collection.size()];
            int index = 0;
            for (Object item : collection) {
                objects[index++] = item;
            }
            return objects;
        }
    }
    /**
     * 集合转数组
     * @param collection
     * @return
     */
    public static String[] toStrArray(Collection<?> collection) {
        if (isEmpty(collection)){
            return EMPTY_STR_ARRAY;
        }else {
            String[] objects = new String[collection.size()];
            int index = 0;
            for (Object item : collection) {
                objects[index++] = Objects.isNull(item) ? null : String.valueOf(item);
            }
            return objects;
        }
    }

    /**
     * 集合转数组
     * @param objects
     * @return
     */
    public static <T> List<T> toList(T[] objects) {
        return Arrays.stream(objects).collect(Collectors.toList());
    }
    /**
     * 集合转数组
     * @param objects
     * @return
     */
    public static <T> Set<T> toSet(T[] objects) {
        return Arrays.stream(objects).collect(Collectors.toSet());
    }

    /**
     * 创建List
     * @param objects
     * @return
     */
    public static <T> List<T> newArrayList(T ... objects) {
        return Arrays.stream(objects).collect(Collectors.toList());
    }

    /**
     * 创建Set
     * @param objects
     * @return
     */
    public static <T> Set<T> newHashSet(T ... objects) {
        return Arrays.stream(objects).collect(Collectors.toSet());
    }

    /**
     * 创建Map
     * @param key
     * @param value
     * @return
     */
    public static <K,V> Map<K,V> newHashMap(K key, V value) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    /**
     * key1,value1,key2,value2
     * @param objects
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K,V> Map<K,V> newHashMap(Object ... objects) {
        Assert.isTrue(objects.length % 2 == 0, "参数值必须为偶数");
        Map<K, V> map = new HashMap<>();
        int keyIndex = 0;
        while (keyIndex < objects.length){
            map.put((K)objects[keyIndex], (V)objects[++keyIndex]);
            keyIndex++;
        }
        return map;
    }
}
