package com.kancy.spring.minidb.serialize;

import com.alibaba.fastjson.JSON;
import com.github.kancyframework.springx.utils.IoUtils;
import com.kancy.spring.minidb.ObjectConfig;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Set;

/**
 * FastJsonObjectDataSerializer
 *
 * @author kancy
 * @date 2021/1/12 20:11
 */
public class FastJsonObjectDataSerializer implements ObjectDataSerializer {

    @Override
    public <T extends ObjectConfig> void write(T objectData, OutputStream outputStream) throws Exception {
        String string = JSON.toJSONString(objectData);
        IoUtils.copy(new ByteArrayInputStream(string.getBytes()), outputStream);
    }

    @Override
    public <T extends ObjectConfig> T read(InputStream inputStream, Class<T> cls) throws Exception {
        byte[] bytes = IoUtils.toByteArray(inputStream);
        return JSON.parseObject(new String(bytes), cls);
    }

    @Override
    public Set<String> hasClassesOnCondition() {
        return Collections.singleton("com.alibaba.fastjson.JSON");
    }

    @Override
    public String getSerializableType() {
        return "fastjson";
    }
}
