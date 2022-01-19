package com.kancy.imagebeder.config;

import com.kancy.spring.minidb.ObjectData;
import lombok.Data;

import java.util.Map;
import java.util.TreeMap;

/**
 * Configs
 *
 * @author huangchengkang
 * @date 2022/1/19 0:31
 */
@Data
public class Settings extends ObjectData {

    private Map<String, ImagebedConfig> configs = new TreeMap<>();

    private boolean linkEnabled = true;

}
