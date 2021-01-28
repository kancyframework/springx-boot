package com.kancy.spring.minidb;

import com.github.kancyframework.springx.log.Logger;
import com.github.kancyframework.springx.log.LoggerFactory;
import com.github.kancyframework.springx.utils.CollectionUtils;
import com.github.kancyframework.springx.utils.SpiUtils;
import com.kancy.spring.minidb.serialize.JDKObjectDataSerializer;
import com.kancy.spring.minidb.serialize.ObjectDataSerializer;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * ObjectDataSerializableHolder
 *
 * @author kancy
 * @date 2021/1/12 15:28
 */
public class ObjectDataSerializerHolder {

    private static Logger log = LoggerFactory.getLogger(ObjectDataSerializerHolder.class);

    private static ObjectDataSerializer serializable;

    static {
        List<ObjectDataSerializer> services = null;
        try {
            services = SpiUtils.findServices(ObjectDataSerializer.class);

            if (!CollectionUtils.isEmpty(services)){
                Optional<ObjectDataSerializer> optional = services.stream().filter(s -> s.isSupport()).findFirst();
                if (optional.isPresent()){
                    serializable = optional.get();
                }
            }

        } catch (Exception e) {
            log.debug("spi find ObjectDataSerializable fail : {}", e.getMessage());
        }
        if (Objects.isNull(serializable)){
            serializable = new JDKObjectDataSerializer();
        }
        log.info("使用 {} 序列化组件", serializable.getSerializableType());
    }

    public static ObjectDataSerializer get() {
        return serializable;
    }
}
