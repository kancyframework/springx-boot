package com.github.kancyframework.springx.context.env;

import com.github.kancyframework.springx.annotation.Order;
import com.github.kancyframework.springx.log.Logger;
import com.github.kancyframework.springx.log.LoggerFactory;
import com.github.kancyframework.springx.utils.PathUtils;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * AppConfigEnvironmentPostProcessor
 *
 * @author kancy
 * @date 2021/1/9 11:24
 */
@Order(-100)
public class AppConfigEnvironmentPostProcessor implements EnvironmentPostProcessor {
    private Logger log = LoggerFactory.getLogger(AppConfigEnvironmentPostProcessor.class);

    @Override
    public void postProcessEnvironment(Environment environment) {
        // 将本地配置放到环境中
        String fileName = String.format("%s.properties", environment.getApplicationName());
        String path = PathUtils.path(System.getProperty("user.home"), "springx-boot", fileName);
        ProfileProperties profileProperties = new ProfileProperties(String.format("app-conf-%s", environment.getApplicationName()));
        try {
            profileProperties.load(new FileInputStream(path));
            log.warn("load local app config file success : {}", fileName);
        } catch (IOException e) {
            log.warn("load local app config file [{}] fail : {}", fileName, e.getMessage());
        }
        environment.getEnvironmentProperties().putAll(profileProperties);
    }
}
