package com.github.kancyframework.springx.context;

/**
 * InitializingBean
 *
 * @author kancy
 * @date 2021/1/13 0:17
 */
public interface InitializingBean {
    /**
     * Invoked by the containing {@code BeanFactory} after it has set all bean properties
     * and satisfied, {@code ApplicationContextAware} etc.
     */
    void afterPropertiesSet();
}