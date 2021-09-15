package com.kancy.spring.minidb;

import java.io.Serializable;

/**
 * DataConfig
 *
 * @author huangchengkang
 * @date 2021/9/16 1:42
 */
public interface ObjectConfig {
    /**
     * 获取数据对象ID
     * @return
     */
    String getId();
    /**
     * 获取数据对象ID
     * @return
     */
    void setId(String id);

    /**
     * 保存数据
     * @return
     */
    boolean save();

    /**
     * 获取属性
     * @param name
     * @param <T>
     * @return
     */
    <T extends Serializable> T getProperty(String name);

    /**
     * 获取属性
     * @param name
     * @param <T>
     * @return
     */
    <T extends Serializable> T getProperty(String name, T def);


    /**
     * 获取属性
     * @param name
     * @param <T>
     * @return
     */
    void setProperty(String name,  Serializable value);
}
