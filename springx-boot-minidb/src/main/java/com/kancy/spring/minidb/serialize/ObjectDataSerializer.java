package com.kancy.spring.minidb.serialize;

import com.github.kancyframework.springx.utils.CollectionUtils;
import com.kancy.spring.minidb.ObjectData;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Set;

/**
 * ObjectDataSerializable
 *
 * @author kancy
 * @date 2021/1/12 15:18
 */
public interface ObjectDataSerializer {

    <T extends ObjectData> void write(T objectData, OutputStream outputStream) throws Exception;

    <T extends ObjectData> T read(InputStream inputStream, Class<T> cls) throws Exception;

    String getSerializableType();

    default boolean isSupport(){
        Set<String> classes = hasClassesOnCondition();
        if (CollectionUtils.isEmpty(classes)){
            return true;
        } else {
            for (String aClass : classes) {
                try {
                    Class.forName(aClass);
                } catch (ClassNotFoundException e) {
                    return false;
                }
            }
        }
        return true;
    }

    default Set<String> hasClassesOnCondition(){
        return Collections.emptySet();
    }

}
