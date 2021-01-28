package com.github.kancyframework.springx.context.env;

import com.github.kancyframework.springx.log.Logger;
import com.github.kancyframework.springx.log.LoggerFactory;
import com.github.kancyframework.springx.utils.CollectionUtils;
import com.github.kancyframework.springx.utils.SpiUtils;
import com.github.kancyframework.springx.utils.StringUtils;

import java.util.*;

/**
 * Environment
 *
 * @author kancy
 * @date 2020/2/18 16:33
 */
public class Environment {

    private static final Logger log = LoggerFactory.getLogger(Environment.class);
    private List<String> profiles = new ArrayList();
    private ProfileProperties environmentProperties = new ProfileProperties("environment");
    private ProfileProperties systemProperties = new ProfileProperties("system");
    private ProfileProperties applicationProperties = new ProfileProperties("application");

    private LinkedList<ProfileProperties> properties = new LinkedList<>();

    public Environment() {
        initEnvironment();
        postProcess();
        log.info("Init application [{}] environment finished , active profiles {}",
                getApplicationName(),
                getProfiles());
    }

    private void postProcess() {
        List<EnvironmentPostProcessor> services = SpiUtils.findServices(EnvironmentPostProcessor.class);
        services.forEach(p -> p.postProcessEnvironment(this));
    }

    private void initEnvironment() {
        // 保存系统属性
        systemProperties.putAll(System.getProperties());
        systemProperties.putAll(System.getenv());

        // 使用默认文件
        applicationProperties.putAll(loadProperties("default"));

        ProfileProperties includeProperties = new ProfileProperties("application-include");
        Set<String> profileIncludes = applicationProperties.getSetProperty("spring.profiles.include");
        if (!CollectionUtils.isEmpty(profileIncludes)){
            for (String profileInclude : profileIncludes) {
                includeProperties.putAll(loadProperties(profileInclude));
            }
        }

        // 指定环境
        Set<String> activeProfiles = systemProperties.getSetProperty("spring.profiles.active");
        if (CollectionUtils.isEmpty(activeProfiles)){
            activeProfiles =applicationProperties.getSetProperty("spring.profiles.active");
        }
        if (!CollectionUtils.isEmpty(activeProfiles)){
            for (String activeProfile : activeProfiles) {
                includeProperties.putAll(loadProperties(activeProfile));
            }
        }

        // 合并
        applicationProperties.putAll(includeProperties);
        environmentProperties.putAll(applicationProperties);
        environmentProperties.putAll(systemProperties);

        properties.addFirst(systemProperties);
        properties.addFirst(includeProperties);
        properties.addFirst(applicationProperties);
    }

    private ProfileProperties loadProperties(String envName){
        ProfileProperties profileProperties = new ProfileProperties(String.format("application-%s", envName));
        if (StringUtils.isNotEmpty(envName)){
            profiles.add(envName);
            String profileFileName = "application.properties";
            if (!Objects.equals("default", envName)){
                profileFileName = String.format("application-%s.properties", envName);
            }
            try {
                log.info("load profile [{}] properties file : {}", envName, profileFileName);
                profileProperties.load(Environment.class.getClassLoader().getResourceAsStream(profileFileName));
            } catch (Exception e) {
                log.warn("profile [{}] properties [{}] file load fail.", envName, profileFileName);
            }
        }
        return profileProperties;
    }

    public ProfileProperties getEnvironmentProperties() {
        return environmentProperties;
    }

    protected ProfileProperties getSystemProperties() {
        return systemProperties;
    }

    protected ProfileProperties getApplicationProperties() {
        return applicationProperties;
    }

    public List<String> getProfiles() {
        return profiles;
    }

    public String getApplicationName(){
        return environmentProperties.getStringProperty("spring.application.name", "application");
    }

    public String getStringProperty(String key){
        return getStringProperty(key,null);
    }

    public String getStringProperty(String key, String def){
        return getEnvironmentProperties().getStringProperty(key, def);
    }

    public Boolean getBooleanProperty(String key){
        return getEnvironmentProperties().getBooleanProperty(key);
    }

    public Boolean getBooleanProperty(String key, Boolean def){
        return getEnvironmentProperties().getBooleanProperty(key, def);
    }

    public Integer getIntegerProperty(String key){
        return getEnvironmentProperties().getIntegerProperty(key);
    }

    public Integer getIntegerProperty(String key, Integer def){
        return getEnvironmentProperties().getIntegerProperty(key, def);
    }

    public Long getLongProperty(String key){
        return getEnvironmentProperties().getLongProperty(key);
    }

    public Long getLongProperty(String key, Long def){
        return getEnvironmentProperties().getLongProperty(key, def);
    }

    public Double getDoubleProperty(String key){
        return getEnvironmentProperties().getDoubleProperty(key);
    }

    public Double getDoubleProperty(String key, Double def){
        return getEnvironmentProperties().getDoubleProperty(key, def);
    }

    public List<String> getListProperty(String key){
        return getEnvironmentProperties().getListProperty(key);
    }

    public Set<String> getSetProperty(String key){
        return getEnvironmentProperties().getSetProperty(key);
    }
}
