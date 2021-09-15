package com.kancy.spring.minidb.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.kancyframework.springx.utils.IoUtils;
import com.kancy.spring.minidb.ObjectConfig;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Set;

/**
 * KryoObjectDataSerializable
 *
 * @author kancy
 * @date 2021/1/12 15:40
 */
public class KryoObjectDataSerializer implements ObjectDataSerializer {

    private final Kryo kryo = new Kryo();

    @Override
    public <T extends ObjectConfig> void write(T objectData, OutputStream outputStream) throws Exception {
        Output output = null;
        try {
            Kryo kryo = new Kryo();
            output = new Output(outputStream);
            kryo.writeObject(output, objectData);
        } finally {
            IoUtils.closeResource(output);
        }
    }

    @Override
    public <T extends ObjectConfig> T read(InputStream inputStream, Class<T> cls) throws Exception {
        Input input = null;
        try {
            input = new Input(inputStream);
            return kryo.readObject(input, cls);
        } finally {
            IoUtils.closeResource(input);
        }
    }

    @Override
    public Set<String> hasClassesOnCondition() {
        return Collections.singleton("com.esotericsoftware.kryo.Kryo");
    }

    @Override
    public String getSerializableType() {
        return "kryo";
    }
}
