package com.github.kancyframework.springx.utils;

import com.github.kancyframework.springx.registry.StringRedis;

public class StringRedisUtils {
    private static StringRedis redis = new StringRedis("common");

    /**
     * 设置
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        redis.set(key, value);
    }

    /**
     * 获取
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return redis.get(key);
    }

    /**
     * 删除
     *
     * @param key
     */
    public void delete(String key) {
        redis.delete(key);
    }

    /**
     * 获取数据库名称
     *
     * @return
     */
    public String getDatabase() {
        return redis.getDatabase();
    }

    /**
     * 获取大小
     *
     * @return
     */
    public long getSize() {
        return redis.getSize();
    }
}
