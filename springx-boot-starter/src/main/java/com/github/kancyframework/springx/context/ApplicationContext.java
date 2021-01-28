package com.github.kancyframework.springx.context;

import com.github.kancyframework.springx.context.env.EnvironmentCapable;
import com.github.kancyframework.springx.context.event.ApplicationEventPublisher;
import com.github.kancyframework.springx.context.factory.ListableBeanFactory;

/**
 * ApplicationContext
 *
 * @author kancy
 * @date 2020/2/18 12:12
 */
public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, ApplicationEventPublisher {

    /**
     * 启动时间
     * @param startupDate
     */
    void setStartupDate(long startupDate);

    /**
     * 获取启动时间
     * @return
     */
    long getStartupDate();
}
