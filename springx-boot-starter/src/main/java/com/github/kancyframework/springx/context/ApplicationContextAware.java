package com.github.kancyframework.springx.context;

/**
 * ApplicationContext
 *
 * @author kancy
 * @date 2020/2/18 12:12
 */
public interface ApplicationContextAware {
    /**
     * 上下文
     * @param applicationContext
     */
    void onApplicationContext(ApplicationContext applicationContext);
}
