package com.kancy.spring.minidb;

import com.github.kancyframework.springx.log.Logger;
import com.github.kancyframework.springx.log.LoggerFactory;
import com.github.kancyframework.springx.utils.ObjectUtils;
import com.github.kancyframework.springx.utils.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * ObjectData
 *
 * @author kancy
 * @date 2021/1/8 23:36
 */
public abstract class ObjectData implements ObjectConfig, Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(ObjectData.class);

    private static final ThreadLocal<Boolean> autoCommit = ThreadLocal.withInitial(() -> true);

    private String id;

    protected ObjectData() {
    }

    /**
     * 获取数据对象ID
     * @return
     */
    public final String getId(){
        return id;
    }

    /**
     * 获取数据对象ID
     * @return
     */
    public final void setId(String id){
        this.id = id;
    }

    /**
     * 持久化
     */
    public final boolean save() {
        if (autoCommit.get()){
            ObjectDataManager.store(getClass());
            return true;
        }
        return false;
    }

    /**
     * 开始事务
     */
    public final void tx() {
        autoCommit.set(false);
    }

    /**
     * 提交事务
     */
    public final void commit() {
        if (!autoCommit.get()){
            try {
                ObjectDataManager.store(getClass());
            } finally {
                autoCommit.remove();
            }
        } else {
            log.warn("事务未开启，无法提交数据！");
        }
    }

    /**
     * 获取属性
     *
     * @param name
     * @return
     */
    @Override
    public <T extends Serializable> T getProperty(String name) {
        return (T) ReflectionUtils.getField(name, this);
    }

    /**
     * 获取属性
     *
     * @param name
     * @param valueType
     * @return
     */
    public <T extends Serializable> T getProperty(String name, Class<T> valueType) {
        return ReflectionUtils.getField(name, this, valueType);
    }

    /**
     * 获取属性
     *
     * @param name
     * @param def
     * @return
     */
    @Override
    public <T extends Serializable> T getProperty(String name, T def) {
        Object propertyValue = ReflectionUtils.getField(name, this);
        if (Objects.isNull(propertyValue)){
            return def;
        }
        if (Objects.nonNull(def)){
            return (T) ObjectUtils.cast(propertyValue, def.getClass());
        }
        return (T) propertyValue;
    }

    /**
     * 获取属性
     *
     * @param name
     * @param value
     * @return
     */
    @Override
    public void setProperty(String name, Serializable value) {
        Field field = ReflectionUtils.findField(getClass(), name);
        if(Objects.nonNull(field)){
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, this,
                    ObjectUtils.cast(value, field.getType()));
        }
    }
}
