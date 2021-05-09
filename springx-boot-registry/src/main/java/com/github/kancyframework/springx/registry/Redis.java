package com.github.kancyframework.springx.registry;

public interface Redis<T> {
    /**
     * 设置
     * @param key
     * @param value
     */
    void set(String key, T value);

    /**
     * 获取
     * @param key
     * @return
     */
    T get(String key);

    /**
     * 删除
     * @param key
     */
    void delete(String key);

    /**
     * 情空
     */
    void clear();

    /**
     * 获取数据库名称
     * @return
     */
    String getDatabase();

    /**
     * 获取大小
     * @return
     */
    long getSize();
}
