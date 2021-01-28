package com.github.kancyframework.springx.boot;

import com.github.kancyframework.springx.context.*;
import com.github.kancyframework.springx.log.LoggerFactory;
import com.github.kancyframework.springx.utils.OrderUtils;
import com.github.kancyframework.springx.utils.SpringUtils;
import com.github.kancyframework.springx.log.Logger;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * SpringBootApplication
 *
 * @author kancy
 * @date 2020/2/18 12:12
 */
public class SpringApplication {
    private Class<?> applicationEntryClass;

    private ApplicationContext applicationContext;

    private final Logger log = LoggerFactory.getLogger(SpringApplication.class);

    public SpringApplication(Class<?> applicationEntryClass) {
        this.applicationEntryClass = applicationEntryClass;
    }

    public static void run(Class<?> applicationEntryClass, String[] args) {
        new SpringApplication(applicationEntryClass).run(args);
    }

    public void run(String[] args) {
        log.info("start running......");

        // create application context and application initializer
        applicationContext = createSimpleApplicationContext();

        SpringUtils.setApplicationContext(applicationContext);
        ApplicationContextInitializer initializer = createSimpleApplicationContextInitializer(applicationEntryClass);

        // initialize the application context (this is where we create beans)
        // here maybe exist a hidden cast
        initializer.initialize(applicationContext);

        // InitializingBean
        Map<String, InitializingBean> beanMaps = applicationContext.getBeansOfType(InitializingBean.class);
        beanMaps.values().forEach(InitializingBean::afterPropertiesSet);

        // process those special beans
        processSpecialBeans(args);

        log.info("Started {} success in {} seconds!",
                applicationContext.getEnvironment().getApplicationName(),
                (System.currentTimeMillis() *1.0 - applicationContext.getStartupDate())/1000);
    }

    private SimpleApplicationContextInitializer createSimpleApplicationContextInitializer(Class<?> entryClass) {
        // get base packages
        SpringBootApplication annotation = entryClass.getDeclaredAnnotation(SpringBootApplication.class);
        String[] basePackages = annotation.basePackages();
        if (basePackages.length == 0) {
            basePackages = new String[]{entryClass.getPackage().getName()};
        }

        // create context initializer with base packages
        return new SimpleApplicationContextInitializer(Arrays.asList(basePackages));
    }

    private SimpleApplicationContext createSimpleApplicationContext() {
        return new SimpleApplicationContext();
    }

    private void processSpecialBeans(String[] args) {
        callRegisteredRunners(args);
    }

    private void callRegisteredRunners(String[] args) {
        Map<String, ApplicationRunner> applicationRunners = applicationContext.getBeansOfType(ApplicationRunner.class);
        List<ApplicationRunner> applicationRunnerList = sortByOrder(applicationRunners.values());
        try {
            for (ApplicationRunner applicationRunner : applicationRunnerList) {
                applicationRunner.run(args);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过Order注解排序
     * @param collection
     */
    private <T> List<T> sortByOrder(Collection<T> collection) {
        return OrderUtils.sort(collection);
    }
}