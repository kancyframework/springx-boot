package com.github.kancyframework.springx.context.env;

/**
 * EnvironmentPostProcessor
 *
 * @author kancy
 * @date 2021/1/9 11:05
 */
public interface EnvironmentPostProcessor {
    /**
     * 处理
     * @param environment
     */
    void postProcessEnvironment(Environment environment);
}
