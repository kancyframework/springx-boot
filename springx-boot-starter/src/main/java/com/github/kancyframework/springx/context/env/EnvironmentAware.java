package com.github.kancyframework.springx.context.env;

/**
 * EnvironmentAware
 *
 * @author kancy
 * @date 2020/12/13 3:23
 */
public interface EnvironmentAware {
    /**
     * 环境
     * @param environment
     */
    void onEnvironment(Environment environment);
}
