package com.kancy.spring.minidb;

import com.github.kancyframework.springx.log.Logger;
import com.github.kancyframework.springx.log.LoggerFactory;

import java.io.Serializable;

/**
 * ObjectData
 *
 * @author kancy
 * @date 2021/1/8 23:36
 */
public abstract class ObjectData implements Serializable {

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

}
