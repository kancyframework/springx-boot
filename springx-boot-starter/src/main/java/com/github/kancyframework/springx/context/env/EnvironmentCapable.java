package com.github.kancyframework.springx.context.env;

/**
 * EnvironmentCapable
 *
 * @author kancy
 * @date 2020/2/18 17:40
 */
public interface EnvironmentCapable {
    /**
     * Return the {@link Environment} associated with this component.
     * @return
     */
    Environment getEnvironment();
}
