package com.kancy.spring.minidb.serialize;

import com.github.kancyframework.springx.utils.IoUtils;
import com.kancy.spring.minidb.ObjectConfig;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * JDKObjectDataSerializable
 *
 * @author kancy
 * @date 2021/1/12 15:21
 */
public class JDKObjectDataSerializer implements ObjectDataSerializer {
    @Override
    public <T extends ObjectConfig> void write(T objectData, OutputStream outputStream) throws Exception {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(objectData);
        } finally {
            IoUtils.closeResource(outputStream);
        }

    }

    @Override
    public <T extends ObjectConfig> T read(InputStream inputStream, Class<T> cls) throws Exception {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            return (T) objectInputStream.readObject();
        } finally {
            IoUtils.closeResource(inputStream);
        }
    }

    @Override
    public String getSerializableType() {
        return "jdk";
    }
}
