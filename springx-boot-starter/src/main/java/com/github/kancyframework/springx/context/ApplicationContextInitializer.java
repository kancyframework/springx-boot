package com.github.kancyframework.springx.context;

/**
 * ApplicationContextInitializer
 *
 * @author kancy
 * @date 2020/2/18 12:12
 */
public interface ApplicationContextInitializer<C extends ApplicationContext> {
    /**
     * 容器初始化
     * @param applicationContext
     */
    void initialize(C applicationContext);
}