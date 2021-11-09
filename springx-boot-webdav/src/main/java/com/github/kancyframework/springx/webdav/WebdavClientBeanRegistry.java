package com.github.kancyframework.springx.webdav;

import com.github.kancyframework.springx.context.ApplicationContext;
import com.github.kancyframework.springx.context.DynamicBeanRegistry;
import com.github.kancyframework.springx.context.env.Environment;
import com.github.kancyframework.springx.context.factory.BeanDefinition;
import com.github.kancyframework.springx.utils.IDUtils;
import com.github.kancyframework.springx.utils.SpringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * WebdavClientBeanRegistry
 *
 * @author huangchengkang
 * @date 2021/10/21 11:23
 */
public class WebdavClientBeanRegistry implements DynamicBeanRegistry {
    /**
     * 注册Beans
     * key -> beanName
     * value -> BeanDefinition
     *
     * @param applicationContext
     * @return
     */
    @Override
    public Map<String, BeanDefinition> registerBeans(ApplicationContext applicationContext) {
        Map<String, BeanDefinition> beanMap = new HashMap<>();
        if (!SpringUtils.existBean(WebdavClient.class)){
            String beanName = String.format("webdavClient-%s", IDUtils.getUUIDString());
            WebdavClient webdavClient = initWebdavClient(applicationContext);
            beanMap.put(beanName, new BeanDefinition(webdavClient, webdavClient.getClass()));
        }
        return beanMap;
    }

    private WebdavClient initWebdavClient(ApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();
        String baseUrl = environment.getStringProperty("webdav.baseUrl", "https://dav.jianguoyun.com/dav");
        String username = environment.getStringProperty("webdav.username", "793272861@qq.com");
        String password = environment.getStringProperty("webdav.password", "a7cxqrsftadqkema");
        String contextPath = environment.getStringProperty("webdav.contextPath", "/");
        WebdavClientImpl webdavClient = new WebdavClientImpl(baseUrl, username, password, contextPath);
        return webdavClient;
    }
}
