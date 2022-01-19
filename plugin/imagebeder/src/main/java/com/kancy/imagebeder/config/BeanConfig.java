package com.kancy.imagebeder.config;

import com.github.kancyframework.springx.context.annotation.Bean;
import com.github.kancyframework.springx.context.annotation.Configuration;
import com.kancy.spring.minidb.ObjectDataManager;

/**
 * BeanConfig
 *
 * @author huangchengkang
 * @date 2022/1/19 0:55
 */
@Configuration
public class BeanConfig {
    @Bean
    public Settings settings(){
        return ObjectDataManager.load(Settings.class);
    }
}
