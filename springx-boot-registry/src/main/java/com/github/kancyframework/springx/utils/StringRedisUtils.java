package com.github.kancyframework.springx.utils;

import com.github.kancyframework.springx.registry.StringRedis;

import java.util.Objects;

public class StringRedisUtils {
    private static StringRedis redis = new StringRedis("common");

    /**
     * 设置
     *
     * @param key
     * @param value
     */
    public static void set(String key, String value) {
        redis.set(key, value);
    }


    /**
     * 设置
     *
     * @param key
     * @param value
     */
    public static void set(String key, Number value) {
        redis.set(key, Objects.isNull(value) ? null : String.valueOf(value));
    }

    /**
     * 获取
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        return redis.get(key);
    }

    /**
     * 删除
     *
     * @param key
     */
    public static void delete(String key) {
        redis.delete(key);
    }

    /**
     * 获取数据库名称
     *
     * @return
     */
    public static String getDatabase() {
        return redis.getDatabase();
    }

    /**
     * 获取大小
     *
     * @return
     */
    public static long getSize() {
        return redis.getSize();
    }
}
